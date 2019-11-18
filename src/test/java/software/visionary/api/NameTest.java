package software.visionary.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

final class NameTest {
    @Test
    void doesNotAllowNull() {
        Assertions.assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    void doesNotAllowEmpty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Name(""));
    }

    @Test
    void doesNotAllowOnlyWhitespace() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Name("   "));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Name(String.format("\t\t %n")));
    }

    @Test
    void delegatesLength() {
        final String string = "cool stuff";
        final Name toTest = new Name(string);
        Assertions.assertEquals(string.length(), toTest.length());
    }

    @Test
    void delegatesCharAt() {
        final String string = "cool stuff";
        final Name toTest = new Name(string);
        for (int i = 0; i< string.length(); i++) {
            Assertions.assertEquals(string.charAt(i), toTest.charAt(i));
        }
    }

    @Test
    void delegatessubSequence() {
        final String string = "cool stuff";
        final Name toTest = new Name(string);
        final CharSequence subFromString = string.substring(0, 2);
        final CharSequence subFromName = toTest.subSequence(0, 2);
        Assertions.assertEquals(subFromString, subFromName);
    }

    @Test
    void hashCodeIsFromSource() {
        final String string = "cool stuff";
        final Name toTest = new Name(string);
        Assertions.assertEquals(Objects.hash(string), toTest.hashCode());
    }

    @Test
    void equalsContractIsCorrect() {
        final String string = "cool stuff";
        final Name toTest = new Name(string);
        Assertions.assertFalse(toTest.equals(null));
        Assertions.assertFalse(toTest.equals(string));
        Assertions.assertTrue(toTest.equals(toTest));
        final Name shouldEqual = new Name(string);
        Assertions.assertTrue(toTest.equals(shouldEqual));
        Assertions.assertTrue(shouldEqual.equals(toTest));
    }


    @Test
    void toStringIsFromSource() {
        final String string = "cool stuff";
        final Name toTest = new Name(string);
        Assertions.assertEquals(string, toTest.toString());
    }
}
