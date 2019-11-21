package software.visionary.muncher;

import software.visionary.api.Mainable;

import java.io.InputStream;
import java.io.PrintStream;

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
        final Runnable toRun = (getArgs().size() == 1) ? new RecollectMealsFromLastWeek(getArgs().toArray(new String[0])) : new RecollectMealsFromTimeRange(getArgs().toArray(new String[0]));
        toRun.run();
    }

    public static void main(final String[] args) {
        new RecollectMeals(args, System.in, System.out).run();
    }
}
