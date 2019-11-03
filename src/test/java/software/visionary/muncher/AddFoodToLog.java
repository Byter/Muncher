package software.visionary.muncher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class AddFoodToLog {
    @Test
    void canConsumeFood() {
        // Given: the existence of a muncher
        final Muncher nick = Fixtures.createMuncher();
        // And: Some food to be eaten
        final Food boneBroth = Fixtures.createBoneBroth();
        // When: the muncher eats the food
        nick.eat(boneBroth);
        // And: I ask the muncher about the food they've eaten
        final Fixtures.WhatDidYouEat question = Fixtures.createFoodQuestion();
        nick.ask(question);
        // Then: the answer contains the food
        Assertions.assertTrue(question.hasEaten(boneBroth));
    }
}
