package software.visionary.muncher;

import software.visionary.api.Storable;
import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.Meal;
import software.visionary.muncher.api.MutableMeal;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Objects;

public enum FunctionalLogMealMicroservice {
    INSTANCE;

    public static void main(final String[] args) {
        if (args.length == 0 || Arrays.toString(args).contains("help")) {
            printHelp();
        }
        new LogAMeal(args).run();
    }

    private static void printHelp() {
        System.out.println("Welcome to Visionary Software Solutions Meal Logging service!");
        System.out.println("The first argument should be a name, which is a non-null, non-empty String. This is used to identify the user data to save.");
        System.out.println("The second argument should be the year-month-day and time the meal started, e.g. 2019-11-05T23:45");
        System.out.println("The third argument should be the year-month-day and time the meal ended, e.g. 2019-11-05T23:59");
        System.out.println("Every argument thereafter should be a string representing food to add to the meal, e.g. \"Progresso Minestrone Soup\"");
    }

    private static class LogAMeal implements Runnable {
        private final Deque<String> args;

        LogAMeal(final String[] args) {
            this.args = new ArrayDeque<>(Arrays.asList(args));
        }

        @Override
        public void run() {
            final Name theMuncher = new Name(Objects.requireNonNull(args.pop()));
            final Storable<Meal> user = new PersistToFileMuncher(theMuncher);
            final LocalDateTime startedAt = LocalDateTime.parse(args.pop(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            final LocalDateTime endedAt = LocalDateTime.parse(args.pop(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            final MutableMeal toLog = new InMemoryMeal(startedAt.toInstant(ZoneOffset.UTC), endedAt.toInstant(ZoneOffset.UTC));
            while (!args.isEmpty()) {
                final Food f = new InMemoryFood(new Name(args.pop()));
                System.out.println("Adding the food " + f);
                toLog.store(f);
            }
            user.store(toLog);
        }
    }
}
