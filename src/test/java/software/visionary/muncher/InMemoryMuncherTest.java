package software.visionary.muncher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.visionary.api.Name;
import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.Meal;

import java.util.ArrayList;
import java.util.List;

final class InMemoryMuncherTest {
    private InMemoryMuncher toTest;

    @BeforeEach
    void setup() {
        toTest = new InMemoryMuncher(new Name("me"));
    }

    @Test
    void rejectsNullName() {
        Assertions.assertThrows(NullPointerException.class, () -> new InMemoryMuncher(null));
    }

    @Test
    void rejectsStoringNullMeal() {
        Assertions.assertThrows(NullPointerException.class, () -> toTest.store(null));
    }

    @Test
    void initiallyEmpty() {
        final List<Meal> stored = new ArrayList<>();
        toTest.query(stored::add);
        Assertions.assertTrue(stored.isEmpty());
    }

    @Test
    void canStoreAndQuery() {
        final Food food = Fixtures.createFood("String cheese");
        final Meal aMeal = InMemoryMeal.fromFood(food);
        toTest.store(aMeal);
        final List<Meal> stored = new ArrayList<>();
        toTest.query(stored::add);
        Assertions.assertTrue(stored.contains(aMeal));
    }

    @Test
    void toStringIsName() {
        Assertions.assertEquals("me", toTest.toString());
    }
}
