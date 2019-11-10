package software.visionary.muncher;

import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.Foods;
import software.visionary.muncher.api.Meal;
import software.visionary.muncher.api.Muncher;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

final class PersistToFileMuncher implements Muncher {
    private final Name name;

    PersistToFileMuncher(final Name theMuncher) {
        this.name = Objects.requireNonNull(theMuncher);
    }

    @Override
    public synchronized void store(final Meal toStore) {
        final List<Meal> toWrite = new ArrayList<>();
        query(toWrite::add);
        toWrite.add(new SerializedMeal(toStore));
        //TODO: Refactor to Dependency Injection if necessary
        ObjectSerializer.INSTANCE.writeAllObjectsToFile(toWrite, ObjectSerializer.getFileToSaveAs(toString()));
    }

    @Override
    public void query(final Consumer<Meal> visitor) {
        //TODO: Refactor to Dependency Injection if necessary
        ObjectSerializer.INSTANCE.readAllObjects(ObjectSerializer.getFileToSaveAs(toString())).stream().map(SerializedMeal.class::cast).forEach(visitor);
    }

    @Override
    public String toString() { return name.toString(); }

    private static final class SerializedMeal implements Serializable, Meal {

        private static final class SerializedFood implements Serializable, Food {
            private String food;

            @Override
            public Name getName() {
                return new Name(food);
            }

            @Override
            public String toString() {
                return getName().toString();
            }
        }

        private static final class SerializedFoods implements Serializable, Foods {
            private final List<SerializedFood> foods;

            private SerializedFoods() {
                this.foods = new ArrayList<>();
            }

            @Override
            public boolean has(final Food food) {
                return (food instanceof SerializedFood) && foods.contains(food);
            }

            @Override
            public Iterator<Food> iterator() {
                return foods.stream().map(Food.class::cast).iterator();
            }

            @Override
            public String toString() {
                final StringBuilder builder = new StringBuilder();
                foods.forEach(food -> builder.append(String.format("%n%s%n", food)));
                return builder.toString();
            }
        }

        private final Instant start, end;
        private final SerializedFoods foods;

        private SerializedMeal(final Meal meal) {
            start = meal.getStartedAt();
            end = meal.getEndedAt();
            foods = new SerializedFoods();
            meal.getFoods().forEach(f -> {
                final SerializedFood food = new SerializedFood();
                food.food = f.toString();
                foods.foods.add(food);
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
        public Foods getFoods() {
            return foods;
        }

        @Override
        public String toString() {
            return String.format("Started @ %s%nEnded @ %s%nContained:%s%n", start, end, foods);
        }
    }
}
