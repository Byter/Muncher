package software.visionary.muncher;

import software.visionary.muncher.api.Muncher;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public enum RecollectMealsMicroservice {
    INSTANCE;

    public static void main(final String[] args) {
        final Scanner in = new Scanner(System.in);
        System.out.println("Welcome to Muncher, a Visionary Software Solutions Vision for Meal logging!");
        System.out.println("What is your name?");
        final Name theMuncher = new Name(in.nextLine());
        busyWait(theMuncher, in);
    }

    private static void busyWait(final Name theMuncher, final Scanner in) {
        final Muncher user = new PersistToFileMuncher(new InMemoryMuncher(theMuncher));
        do {
            printMenu();
            switch (in.nextLine()) {
                case "0" : recollectMealsFromLastWeek(user); break;
                case "1" : recollectMealsFromTimeRange(user, in); break;
                case "q":
                case "Q":
                    System.exit(1);
                default:
                    throw new UnsupportedOperationException("Please select from valid options");
            }
        } while (true);
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("Choose an action, Q to quit");
        System.out.println("0 - Recollect logged Meals for the last week");
        System.out.println("1 - Recollect logged Meals for a custom time range");
        System.out.println();
    }

    private static void recollectMealsFromLastWeek(final Muncher user) {
        final MealsFromOneWeekAgoToNow query = new MealsFromOneWeekAgoToNow();
        user.recollect(query.andThen(System.out::println));
    }

    private static void recollectMealsFromTimeRange(final Muncher user, final Scanner in) {
        System.out.println("Enter the year-month-day and time the meal started, e.g. 2019-11-05T23:45");
        final LocalDateTime startedAt = LocalDateTime.parse(in.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println("Enter the year-month-day and time the meal ended, e.g. 2019-11-05T23:59");
        final LocalDateTime endedAt = LocalDateTime.parse(in.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        final MealsWithinTimeRange query = new MealsWithinTimeRange(startedAt.toInstant(ZoneOffset.UTC), endedAt.toInstant(ZoneOffset.UTC));
        user.recollect(query.andThen(System.out::println));
    }
}
