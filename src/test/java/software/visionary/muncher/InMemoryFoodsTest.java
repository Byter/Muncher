package software.visionary.muncher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.visionary.muncher.api.Food;

import java.util.ArrayList;
import java.util.List;

final class InMemoryFoodsTest {
    private InMemoryFoods toTest;

    @BeforeEach
    void setup() {
        toTest = new InMemoryFoods();
    }

    @Test
    void rejectsAddingNullFood() {
        Assertions.assertThrows(NullPointerException.class, () -> toTest.store(null));
    }

    @Test
    void initiallyEmpty() {
        final List<Food> stored = new ArrayList<>();
        toTest.query(stored::add);
        Assertions.assertTrue(stored.isEmpty());
    }

    @Test
    void canStoreAndQuery() {
        final Food food = Fixtures.createFood("String cheese");
        toTest.store(food);
        final List<Food> stored = new ArrayList<>();
        toTest.query(stored::add);
        Assertions.assertTrue(stored.contains(food));
    }
}
