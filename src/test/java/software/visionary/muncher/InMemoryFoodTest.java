package software.visionary.muncher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.visionary.api.Name;

final class InMemoryFoodTest {
    @Test
    void rejectsNullName() {
        Assertions.assertThrows(NullPointerException.class, () -> new InMemoryFood(null));
    }

    @Test
    void canBeCreatedWithAName() {
        final Name named = new Name("1 cup crimini mushrooms");
        final InMemoryFood result = new InMemoryFood(named);
        Assertions.assertEquals(named, result.getName());
    }

    @Test
    void toStringIsTheName() {
        final String source = "1 cup crimini mushrooms";
        final Name named = new Name(source);
        final InMemoryFood result = new InMemoryFood(named);
        Assertions.assertEquals(source, result.toString());
    }
}
