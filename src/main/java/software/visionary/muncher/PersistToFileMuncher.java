package software.visionary.muncher;

import software.visionary.api.Name;
import software.visionary.api.ObjectSerializer;
import software.visionary.api.Storable;
import software.visionary.muncher.api.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

final class PersistToFileMuncher implements Storable<Meal> {
    private static final Object LOCK = new Object();
    private final Name name;

    PersistToFileMuncher(final Name theMuncher) {
        this.name = Objects.requireNonNull(theMuncher);
    }

    @Override
    public void store(final Meal toStore) {
        synchronized (LOCK) {
            final List<Meal> toWrite = new ArrayList<>();
            query(toWrite::add);
            toWrite.add(new SerializedMeal(toStore));
            //TODO: Refactor to Dependency Injection if necessary
            ObjectSerializer.INSTANCE.writeAllObjectsToFile(toWrite, ObjectSerializer.getFileToSaveAs(toString()));
        }
    }

    @Override
    public void query(final Consumer<Meal> visitor) {
        synchronized (LOCK) {
            //TODO: Refactor to Dependency Injection if necessary
            ObjectSerializer.INSTANCE.readAllObjects(ObjectSerializer.getFileToSaveAs(toString())).stream().map(SerializedMeal.class::cast).forEach(visitor);
        }
    }

    @Override
    public String toString() { return name.toString(); }

    private static final class SerializedMeal implements Serializable, Meal {

        private static final class SerializedFood implements Serializable, Food {
            private String food;

            private SerializedFood(final Food food) {
                this.food = Objects.requireNonNull(food).getName().toString();
            }

            @Override
            public Name getName() {
                return new Name(food);
            }
        }

        private static final class SerializedFoods implements Serializable, Storable<Food> {
            private final List<SerializedFood> foods;

            private SerializedFoods() {
                this.foods = new ArrayList<>();
            }

            @Override
            public String toString() {
                final StringBuilder builder = new StringBuilder();
                foods.forEach(food -> builder.append(String.format("%n%s%n", food.getName())));
                return builder.toString();
            }

            @Override
            public void store(final Food food) {
                foods.add(new SerializedFood(food));
            }

            @Override
            public void query(final Consumer<Food> visitor) {
                foods.forEach(visitor::accept);
            }
        }

        private final Instant start, end;
        private final SerializedFoods foods;

        private SerializedMeal(final Meal meal) {
            start = meal.getStartedAt();
            end = meal.getEndedAt();
            foods = new SerializedFoods();
            meal.getFoods().query(f -> {
                final SerializedFood food = new SerializedFood(f);
                foods.store(food);
            });
        }

        @Override
        public Instant getStartedAt() {
            return start;
        }

        @Override
        public Instant getEndedAt() {
            return end;
        }

        @Override
        public Storable<Food> getFoods() {
            return foods;
        }

        @Override
        public String toString() {
            return String.format("Started @ %s%nEnded @ %s%nContained:%s%n", start, end, foods);
        }
    }
}
