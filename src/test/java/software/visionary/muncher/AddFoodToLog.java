package software.visionary.muncher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.visionary.api.Storable;
import software.visionary.muncher.api.Food;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class AddFoodToLog {
    @Test
    void canConsumeFood() {
        // Given: the existence of a Eater
        final Storable<Food> nick = Fixtures.createEater();
        // And: Some food to be eaten
        final Food boneBroth = Fixtures.createBoneBroth();
        // When: the muncher eats the food
        nick.store(boneBroth);
        // And: I ask the muncher about the food they've eaten
        final WhatDidYouEat question = new WhatDidYouEat();
        nick.query(question);
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
