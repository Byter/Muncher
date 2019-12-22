package software.visionary.muncher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

final class LogAMealTest {
    @Test
    void writeHelpToOutputStream() {
        final PrintStream old = System.out;
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final PrintStream toTest = new PrintStream(result);
        System.setOut(toTest);
        LogAMeal.main(null);
        Assertions.assertFalse(result.toString().isEmpty());
        System.setOut(old);
    }

    @Test
    void writesMuncherToFile() {
        final String muncher = UUID.randomUUID().toString();
        final LocalDateTime start = LocalDateTime.now().minusDays(1);
        final LocalDateTime end = LocalDateTime.now();
        final String food = "Moon Pie";
        final String[] args = new String[] { muncher, start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), food };
        LogAMeal.main(args);
        final File file = new File(muncher);
        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(file.length() > 0);
        file.deleteOnExit();
    }
}
