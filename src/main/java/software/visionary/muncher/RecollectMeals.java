package software.visionary.muncher;

import software.visionary.api.*;
import software.visionary.muncher.api.Meal;

import java.io.InputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

final class RecollectMeals extends Mainable {

    private RecollectMeals(final String[] args, final InputStream in, final PrintStream out) {
        super(args, in, out);
    }

    @Override
    protected void printHelp() {
        writeToOutput("Welcome to Visionary Software Solutions Meal Logging service!");
        writeToOutput("The first argument should be a name, which is a non-null, non-empty String. This is used to identify the user data to save.");
        writeToOutput("If no additional arguments are given, Meals from the last week will be returned.");
        writeToOutput("if you'd like to query for Meals from a custom time range, they should be the second and third arguments.");
        writeToOutput("The second argument should be the year-month-day and time the meal started, e.g. 2019-11-05T23:45");
        writeToOutput("The third argument should be the year-month-day and time the meal ended, e.g. 2019-11-05T23:59");
    }

    @Override
    public void execute() {
        final Runnable toRun = getToRun();
        toRun.run();
    }

    private Runnable getToRun() {
        final Name name = new Name(getArgs().pop());
        final Queryable<Meal> user = new PersistToFileMuncher(name);
        final Consumer<Meal> query = getQuery();
        return () -> user.query(query.andThen(meal -> writeToOutput(meal.toString())));
    }

    private Consumer<Meal> getQuery() {
        if (getArgs().size() == 0) {
                return new EventsFromOneWeekAgoToNow<>() {};
        } else {
            final String startTime = getArgs().pop();
            final String endTime = getArgs().pop();
            final Instant startedAt = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toInstant(ZoneOffset.UTC);
            final Instant endedAt = LocalDateTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toInstant(ZoneOffset.UTC);
            return new EventsWithinTimeRange<>(startedAt, endedAt) {};
        }
    }

    public static void main(final String[] args) {
        new RecollectMeals(args, System.in, System.out).run();
    }
}
