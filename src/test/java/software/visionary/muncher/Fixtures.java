package software.visionary.muncher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class Fixtures {
    private Fixtures() {};

    static Meal createMealFromXDaysAgo(int daysAgo) {
        final Instant startTime = Instant.now().minus(daysAgo, ChronoUnit.DAYS);
        final Instant endTime = startTime.plus(1, ChronoUnit.HOURS);
        return new InMemoryMeal(startTime, endTime);
    }

    private static final class InMemoryMeal implements Meal {
        private final Instant startTime;
        private final Instant endTime;

        private InMemoryMeal(final Instant startTime, final Instant endTime) {
            this.startTime = Objects.requireNonNull(startTime);
            this.endTime = Objects.requireNonNull(endTime);
        }

        @Override
        public Instant getStartedAt() {
            return startTime;
        }

        @Override
        public Instant getEndedAt() {
            return endTime;
        }
    }

    private static final class InMemoryMuncher implements Muncher {
        private final List<Food> eaten;
        //TODO: fold eaten into consumed by creating a new Meal every time food is eaten
        private final List<Meal> consumed;
        InMemoryMuncher() {
            eaten = new ArrayList<>();
            consumed = new ArrayList<>();
        }

        @Override
        public void eat(Food food) {
            eaten.add(Objects.requireNonNull(food));
        }

        @Override
        public void ask(Consumer<Food> question) {
            eaten.forEach(question::accept);
        }

        @Override
        public void log(Meal meal) {
            consumed.add(Objects.requireNonNull(meal));
        }

        @Override
        public void query(Consumer<Meal> query) {
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

    static final class WhatDidYouEat implements Consumer<Food> {
        private final List<Food> consumed;

        WhatDidYouEat() {
            consumed = new ArrayList<>();
        }

        @Override
        public void accept(Food food) {
            consumed.add(Objects.requireNonNull(food));
        }

        boolean hasEaten(Food food) {
            return consumed.contains(food);
        }
    }

    static WhatDidYouEat createFoodQuestion() {
        return new WhatDidYouEat();
    }
}
