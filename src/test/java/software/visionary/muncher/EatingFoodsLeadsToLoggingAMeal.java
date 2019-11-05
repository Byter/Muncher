package software.visionary.muncher;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;

final class EatingFoodsLeadsToLoggingAMeal {
    @Test
    void consumingFoodResultsInCreationOfAMeal() {
        // Given: the existence of a muncher
        final Muncher nick = Fixtures.createMuncher();
        // And: Some food to be eaten
        final Food boneBroth = Fixtures.createBoneBroth();
        // When: the muncher eats the food
        nick.eat(boneBroth);
        // And: I ask the muncher about the Meals they've had
        final MealsContainingFood query = new MealsContainingFood(boneBroth);
        // When: I query
        nick.recollect(query);
        // Then: the meal is found
        assertTrue(query.foundMealFromFood());
    }

    private static final class MealsContainingFood implements Consumer<Meal> {
        private final Food sought;
        private final List<Meal> meals;

        MealsContainingFood(final Food toFind) {
            sought = Objects.requireNonNull(toFind);
            meals = new ArrayList<>();
        }

        @Override
        public void accept(final Meal meal) {
            if (meal.getFoods().has(sought)) {
                meals.add(meal);
            }
        }

        boolean foundMealFromFood() {
            return !meals.isEmpty();
        }
    }
}