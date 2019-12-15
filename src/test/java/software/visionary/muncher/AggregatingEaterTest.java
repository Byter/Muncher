package software.visionary.muncher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.visionary.api.Storable;
import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.Meal;
import software.visionary.muncher.api.MutableMeal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

final class AggregatingEaterTest {
    Canned dummyStore;
    AggregatingEater toTest;

    @BeforeEach
    void setup() {
        dummyStore = new Canned();
        toTest = new AggregatingEater(dummyStore);
    }

    private static final class Canned implements Storable<Meal> {
        private final List<Meal> meals;

        private Canned() {
            meals = new ArrayList<>();
            meals.add(Fixtures.createMealFromXDaysAgo(1));
            meals.add(Fixtures.createMealFromXHoursAgo(2));
            meals.add(new InMemoryMeal(Instant.now().minus(45, ChronoUnit.MINUTES), Instant.now().minus(30, ChronoUnit.MINUTES)));
        }

        @Override
        public void store(final Meal toStore) {
            meals.add(toStore);
        }

        @Override
        public void query(final Consumer<Meal> visitor) {
            meals.forEach(visitor);
        }
    }

    @Test
    public void rejectsNullStorable() {
        Assertions.assertThrows(NullPointerException.class, () -> new AggregatingEater(null));
    }

    @Test
    public void rejectsNullStartTime() {
        Assertions.assertThrows(NullPointerException.class, () -> new AggregatingEater(new Canned(), null));
    }

    @Test
    void addsNewFoodToMealWithinLastHourByDefault() {
        final Food newFood = Fixtures.createFood("Beecher's cheese curds");
        toTest.store(newFood);
        final List<Food> foods = new ArrayList<>();
        dummyStore.meals.get(2).getFoods().query(foods::add);
        Assertions.assertTrue(foods.contains(newFood));
    }

    @Test
    void createsAMealIfNoMealWithinTimeRange() {
        toTest = new AggregatingEater(dummyStore, Instant.now().minus(20, ChronoUnit.MINUTES));
        final Food newFood = Fixtures.createFood("Beecher's cheese curds");
        toTest.store(newFood);
        Assertions.assertEquals(4, dummyStore.meals.size());
    }

    @Test
    void queryDelegates() {
        final Food one = Fixtures.createFood("Lunchables");
        final Food two = Fixtures.createFood("Protein drink");
        final Food three = Fixtures.createFood("salad");
        ((MutableMeal)dummyStore.meals.get(0)).store(one);
        ((MutableMeal)dummyStore.meals.get(1)).store(two);
        ((MutableMeal)dummyStore.meals.get(2)).store(three);
        final List<Food> foods = new ArrayList<>();
        toTest.query(foods::add);
        Assertions.assertFalse(foods.isEmpty());
        Assertions.assertTrue(foods.contains(one));
        Assertions.assertTrue(foods.contains(two));
        Assertions.assertTrue(foods.contains(three));
    }
}
