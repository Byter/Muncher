package software.visionary.muncher;

import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.Foods;
import software.visionary.muncher.api.Meal;
import software.visionary.muncher.api.Muncher;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

final class PersistToFileMuncher implements Muncher {
    private final Muncher delegate;

    PersistToFileMuncher(final Muncher delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public void eat(final Food food) {
        // TODO: does the whole Meal creation/appending thing need to happen here?
        delegate.eat(food);
    }

    @Override
    public void ask(final Consumer<Food> question) {
        // TODO: does the whole Meal creation/appending thing need to happen here?
        delegate.ask(question);
    }

    @Override
    public synchronized void log(final Meal meal) {
        final VerifyMealsStored previouslyStored = new VerifyMealsStored();
        recollect(previouslyStored);
        final List<SerializedMeal> toWrite = new ArrayList<>(previouslyStored.getStoredMeals().size() + 1);
        toWrite.addAll(previouslyStored.getStoredMeals().stream().map(SerializedMeal.class::cast).collect(Collectors.toList()));
        toWrite.add(new SerializedMeal(meal));
        //TODO: Refactor to Dependency Injection if necessary
        ObjectSerializer.INSTANCE.writeAllObjectsToFile(toWrite, ObjectSerializer.getFileToSaveAs(toString()));
    }

    static final class VerifyMealsStored implements Consumer<Meal> {
        private final List<Meal> stored;

        VerifyMealsStored() {
            stored = new ArrayList<>();
        }

        @Override
        public void accept(final Meal meal) {
            stored.add(meal);
        }

        boolean hasMeal(final Meal toFind) {
            final Optional<Meal> searched = stored.parallelStream()
                    .filter(m -> m.getStartedAt().equals(toFind.getStartedAt()) && m.getEndedAt().equals(toFind.getEndedAt()))
                    .findFirst();
            return searched.isPresent();
        }

        List<Meal> getStoredMeals() { return new ArrayList<>(stored); }
    }

    @Override
    public void recollect(final Consumer<Meal> query) {
        //TODO: Refactor to Dependency Injection if necessary
        ObjectSerializer.INSTANCE.readAllObjects(ObjectSerializer.getFileToSaveAs(toString())).stream().map(SerializedMeal.class::cast).forEach(query);
    }

    @Override
    public String toString() { return delegate.toString(); }

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
