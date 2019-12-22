package software.visionary.muncher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.visionary.api.Name;
import software.visionary.muncher.api.Meal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

final class RecollectMealsTest {
    @Test
    void writeHelpToOutputStream() {
        final PrintStream old = System.out;
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final PrintStream toTest = new PrintStream(result);
        System.setOut(toTest);
        RecollectMeals.main(null);
        Assertions.assertFalse(result.toString().isEmpty());
        System.setOut(old);
    }

    @Test
    void readsMealsFromFileGivenATimeRange() {
        final String muncher = UUID.randomUUID().toString();
        final PersistToFileMuncher elMuncho = new PersistToFileMuncher(new Name(muncher));
        final Meal meal = Fixtures.createMealFromXDaysAgo(1);
        elMuncho.store(meal);
        final LocalDateTime start = LocalDateTime.now().minusDays(1);
        final LocalDateTime end = LocalDateTime.now();
        final String[] args = new String[] { muncher, start.toString(), end.toString()};
        final PrintStream old = System.out;
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final PrintStream toTest = new PrintStream(result);
        System.setOut(toTest);
        RecollectMeals.main(args);
        Assertions.assertTrue(result.toString().contains(meal.getStartedAt().toString()));
        Assertions.assertTrue(result.toString().contains(meal.getEndedAt().toString()));
        final File file = new File(muncher);
        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(file.length() > 0);
        file.deleteOnExit();
        System.setOut(old);
    }

    @Test
    void readsMealsFromFileReturnsMealsWithinLastWeek() {
        final String muncher = UUID.randomUUID().toString();
        final PersistToFileMuncher elMuncho = new PersistToFileMuncher(new Name(muncher));
        final Meal meal = Fixtures.createMealFromXDaysAgo(1);
        elMuncho.store(meal);
        final Meal tooOld = Fixtures.createMealFromXDaysAgo(8);
        elMuncho.store(tooOld);
        final String[] args = new String[] { muncher };
        final PrintStream old = System.out;
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final PrintStream toTest = new PrintStream(result);
        System.setOut(toTest);
        RecollectMeals.main(args);
        Assertions.assertTrue(result.toString().contains(meal.getStartedAt().toString()));
        Assertions.assertTrue(result.toString().contains(meal.getEndedAt().toString()));
        Assertions.assertFalse(result.toString().contains(tooOld.getStartedAt().toString()));
        Assertions.assertFalse(result.toString().contains(tooOld.getEndedAt().toString()));
        final File file = new File(muncher);
        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(file.length() > 0);
        file.deleteOnExit();
        System.setOut(old);
    }
}
