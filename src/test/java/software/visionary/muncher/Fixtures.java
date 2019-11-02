package software.visionary.muncher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class Fixtures {
    private Fixtures() {};

    private static final class InMemoryMuncher implements Muncher {
        private final List<Food> eaten;
        InMemoryMuncher() {
            eaten = new ArrayList<>();
        }

        @Override
        public void eat(Food food) {
            eaten.add(food);
        }

        @Override
        public void ask(Consumer<Food> question) {
            eaten.forEach(question::accept);
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
