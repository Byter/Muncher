package software.visionary.muncher;

import software.visionary.muncher.api.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class Fixtures {
    private Fixtures() {}

    static Meal createMealFromXDaysAgo(final int daysAgo) {
        final Instant startTime = Instant.now().minus(daysAgo, ChronoUnit.DAYS);
        final Instant endTime = startTime.plus(1, ChronoUnit.HOURS);
        return new InMemoryMeal(startTime, endTime);
    }

    static final class InMemoryFoods implements MutableFoods {
        private final List<Food> foods;

        InMemoryFoods() {
            foods = new ArrayList<>();
        }

        @Override
        public boolean has(final Food food) {
            return foods.contains(food);
        }

        @Override
        public void add(final Food food) {
            foods.add(Objects.requireNonNull(food));
        }

        @Override
        public Iterator<Food> iterator() {
            return foods.iterator();
        }
    }

    private static final class InMemoryMeal implements MutableMeal {
        private final Instant startTime;
        private final Instant endTime;
        private final MutableFoods foods;

        private InMemoryMeal(final Instant startTime, final Instant endTime) {
            this.startTime = Objects.requireNonNull(startTime);
            this.endTime = Objects.requireNonNull(endTime);
            this.foods = new InMemoryFoods();
        }

        static Meal fromFood(final Food food) {
            final InMemoryMeal newMeal = new InMemoryMeal(Instant.now(), Instant.now());
            newMeal.add(food);
            return newMeal;
        }

        @Override
        public Instant getStartedAt() {
            return startTime;
        }

        @Override
        public Instant getEndedAt() {
            return endTime;
        }

        @Override
        public Foods getFoods() {
            return foods;
        }

        @Override
        public void add(final Food food) {
            foods.add(Objects.requireNonNull(food));
        }
    }

    private static final class InMemoryMuncher implements Muncher {
        private final List<Meal> consumed;
        InMemoryMuncher() {
            consumed = new ArrayList<>();
        }

        @Override
        public void eat(final Food food) {
            final Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
            final MealsWithinTimeRange anyThingEaten = new MealsWithinTimeRange(oneHourAgo, Instant.now());
            recollect(anyThingEaten);
            anyThingEaten.mostRecent()
                    .ifPresentOrElse(meal -> ((MutableMeal) meal).add(food), () -> log(InMemoryMeal.fromFood(food)));
        }

        @Override
        public void ask(final Consumer<Food> question) {
            consumed.stream()
            .map(Meal::getFoods)
            .forEach(foods -> foods.forEach(question));
        }

        @Override
        public void log(final Meal meal) {
            consumed.add(Objects.requireNonNull(meal));
        }

        @Override
        public void recollect(final Consumer<Meal> query) {
            consumed.forEach(query::accept);
        }
    }

    static Muncher createMuncher() {
        return new InMemoryMuncher();
    }

    private static final class BoneBroth implements Food {

    }

    static Food createBoneBroth() {
        return new BoneBroth();
    }
}
