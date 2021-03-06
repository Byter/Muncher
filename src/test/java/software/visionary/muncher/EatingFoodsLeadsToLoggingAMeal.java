package software.visionary.muncher;

import org.junit.jupiter.api.Test;
import software.visionary.api.Storable;
import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;

final class EatingFoodsLeadsToLoggingAMeal {
    @Test
    void consumingFoodResultsInCreationOfAMeal() {
        // Given: the existence of a muncher
        final Storable<Meal> muncher = Fixtures.createMuncher();
        final Storable<Food> nick = new AggregatingEater(muncher);
        // And: Some food to be eaten
        final Food boneBroth = Fixtures.createBoneBroth();
        // When: the muncher eats the food
        nick.store(boneBroth);
        // And: I ask the muncher about the Meals they've had
        final MealsContainingFood query = new MealsContainingFood(boneBroth);
        // When: I query
        muncher.query(query);
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
            meal.getFoods().query(f -> {
                if (f.equals(sought)) {
                    meals.add(meal);
                }
            });
        }

        boolean foundMealFromFood() {
            return !meals.isEmpty();
        }
    }
}
