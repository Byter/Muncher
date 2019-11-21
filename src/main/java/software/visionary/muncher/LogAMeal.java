package software.visionary.muncher;

import software.visionary.api.Name;
import software.visionary.api.Storable;
import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.Meal;
import software.visionary.muncher.api.MutableMeal;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

final class LogAMeal extends Mainable {

    private LogAMeal(final String[] args, final InputStream in, final OutputStream out) {
        super(args, in, out);
    }

    @Override
    public void execute() {
        final Name theMuncher = new Name(Objects.requireNonNull(getArgs().pop()));
        final Storable<Meal> user = new PersistToFileMuncher(theMuncher);
        final LocalDateTime startedAt = LocalDateTime.parse(getArgs().pop(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        final LocalDateTime endedAt = LocalDateTime.parse(getArgs().pop(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        final MutableMeal toLog = new InMemoryMeal(startedAt.toInstant(ZoneOffset.UTC), endedAt.toInstant(ZoneOffset.UTC));
        while (!getArgs().isEmpty()) {
            final Food f = new InMemoryFood(new Name(getArgs().pop()));
            writeToOutput("Adding the food " + f);
            toLog.store(f);
        }
        user.store(toLog);
    }

    @Override
    protected void printHelp() {
        writeToOutput("Welcome to Visionary Software Solutions Meal Logging service!");
        writeToOutput("The first argument should be a name, which is a non-null, non-empty String. This is used to identify the user data to save.");
        writeToOutput("The second argument should be the year-month-day and time the meal started, e.g. 2019-11-05T23:45");
        writeToOutput("The third argument should be the year-month-day and time the meal ended, e.g. 2019-11-05T23:59");
        writeToOutput("Every argument thereafter should be a string representing food to add to the meal, e.g. \"Progresso Minestrone Soup\"");
    }

    public static void main(final String[] args) {
        new LogAMeal(args, System.in, System.out).run();
    }
}
