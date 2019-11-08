package software.visionary.muncher;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public enum FunctionalRecollectMealsMicroservice {
    INSTANCE;

    public static void main(final String[] args) {
        if (args.length == 0 || Arrays.toString(args).contains("help")) {
            printHelp();
        }

        final Deque<String> toProcess = new ArrayDeque<>(Arrays.asList(args));
        final Name theMuncher = new Name(toProcess.pop());
        if (toProcess.isEmpty()) {
            new RecollectMealsFromLastWeek(new PersistToFileMuncher(theMuncher)).run();
        } else {
            new RecollectMealsFromTimeRange(toProcess.pop(), toProcess.pop(), new PersistToFileMuncher(theMuncher)).run();
        }
    }

    private static void printHelp() {
        System.out.println("Welcome to Visionary Software Solutions Meal Logging service!");
        System.out.println("The first argument should be a name, which is a non-null, non-empty String. This is used to identify the user data to save.");
        System.out.println("If no additional arguments are given, Meals from the last week will be returned.");
        System.out.println("if you'd like to query for Meals from a custom time range, they should be the second and third arguments.");
        System.out.println("The second argument should be the year-month-day and time the meal started, e.g. 2019-11-05T23:45");
        System.out.println("The third argument should be the year-month-day and time the meal ended, e.g. 2019-11-05T23:59");
    }
}
