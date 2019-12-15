package software.visionary.muncher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.Meal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

final class InMemoryMealTest {
    @Test
    void rejectsNullStartTime() {
        Assertions.assertThrows(NullPointerException.class, () -> new InMemoryMeal(null, Instant.now()));
    }

    @Test
    void rejectsEndTime() {
        Assertions.assertThrows(NullPointerException.class, () -> new InMemoryMeal(Instant.now(), null));
    }

    @Test
    void canSuccessfullyConstruct() {
        final Instant start = Instant.now().minus(1, ChronoUnit.HOURS);
        final Instant end = Instant.now();
        final InMemoryMeal toTest = new InMemoryMeal(start, end);
        Assertions.assertEquals(start, toTest.getStartedAt());
        Assertions.assertEquals(end, toTest.getEndedAt());
        Assertions.assertNotNull(toTest.getFoods());
    }

    @Test
    void rejectsFromNullFood() {
        Assertions.assertThrows(NullPointerException.class, () -> InMemoryMeal.fromFood(null));
    }

    @Test
    void fromFoodContainsFood() {
        final Food aFood = Fixtures.createFood("Mozzarella cheese");
        final Meal result = InMemoryMeal.fromFood(aFood);
        final List<Food> found = new ArrayList<>();
        result.getFoods().query(found::add);
        Assertions.assertTrue(found.contains(aFood));
    }

    @Test
    void rejectsStoringNullFood() {
        final Instant start = Instant.now().minus(1, ChronoUnit.HOURS);
        final Instant end = Instant.now();
        final InMemoryMeal toTest = new InMemoryMeal(start, end);
        Assertions.assertThrows(NullPointerException.class, () -> toTest.store(null));
    }

    @Test
    void storesFood() {
        final Instant start = Instant.now().minus(1, ChronoUnit.HOURS);
        final Instant end = Instant.now();
        final InMemoryMeal toTest = new InMemoryMeal(start, end);
        final Food aFood = Fixtures.createFood("Mozzarella cheese");
        toTest.store(aFood);
        final List<Food> found = new ArrayList<>();
        toTest.query(found::add);
        Assertions.assertTrue(found.contains(aFood));
    }
}
