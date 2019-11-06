package software.visionary.muncher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.visionary.muncher.api.Meal;
import software.visionary.muncher.api.Muncher;

import java.nio.file.Files;
import java.nio.file.Paths;

final class PersistMealLogAsAFile {
    @Test
    void canSaveMealsToAFile() {
        // Given: A Muncher that persists data to disk
        final Muncher mom = new PersistToFileMuncher(Fixtures.createMuncher());
        // When: that person logs a Meal from 14 days ago
        final Meal first = Fixtures.createMealFromXDaysAgo(14);
        mom.log(first);
        // Then: A file should exist for that muncher
        Assertions.assertTrue(Files.exists(Paths.get(mom.toString())));
    }

    @Test
    void canLoadMealsFromAFile() {
        // Given: A Muncher that persists data to disk
        final Muncher mom = new PersistToFileMuncher(Fixtures.createMuncher());
        // And: that person logs a Meal from 14 days ago
        final Meal first = Fixtures.createMealFromXDaysAgo(14);
        mom.log(first);
        // And: that person logs a Meal from 3 days ago
        final Meal second = Fixtures.createMealFromXDaysAgo(3);
        mom.log(second);
        // And: that person logs a Meal from 2 days ago
        final Meal third = Fixtures.createMealFromXDaysAgo(2);
        mom.log(third);
        // And: that person logs a Meal from yesterday
        final Meal fourth = Fixtures.createMealFromXDaysAgo(1);
        mom.log(fourth);
        // When: I query the Muncher for all Meals stored
        final PersistToFileMuncher.VerifyMealsStored verifier = new PersistToFileMuncher.VerifyMealsStored();
        mom.recollect(verifier);
        // Then: every meal logged should be stored
        Assertions.assertTrue(verifier.hasMeal(first));
        Assertions.assertTrue(verifier.hasMeal(second));
        Assertions.assertTrue(verifier.hasMeal(third));
        Assertions.assertTrue(verifier.hasMeal(fourth));
    }
}
