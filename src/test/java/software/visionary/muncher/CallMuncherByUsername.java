package software.visionary.muncher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.visionary.muncher.api.Muncher;

final class CallMuncherByUsername {
    @Test
    void munchersShouldHaveAName() {
        // Given: A name the Muncher would like to be known as
        final Name superMuncher = new Name("Super Muncher");
        // When: I create a Muncher with that name
        final Muncher muncher = Fixtures.namedMuncher(superMuncher);
        // Then: The name should be reflected in the Muncher's toString
        Assertions.assertEquals(superMuncher.toString(), muncher.toString());
    }
}