package software.visionary.muncher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class AddFoodToLog {
    @Test
    void canConsumeFood() {
        // Given: the existence of a muncher
        final Eater nick = Fixtures.createMuncher();
        // And: Some food to be eaten
        final Food boneBroth = Fixtures.createBoneBroth();
        // When: the muncher eats the food
        nick.eat(boneBroth);
        // And: I ask the muncher about the food they've eaten
        final WhatDidYouEat question = new WhatDidYouEat();
        nick.ask(question);
        // Then: the answer contains the food
        Assertions.assertTrue(question.hasEaten(boneBroth));
    }

    static final class WhatDidYouEat implements Consumer<Food> {
        private final List<Food> consumed;

        WhatDidYouEat() {
            consumed = new ArrayList<>();
        }

        @Override
        public void accept(final Food food) {
            consumed.add(Objects.requireNonNull(food));
        }

        boolean hasEaten(final Food food) {
            return consumed.contains(food);
        }
    }
}
