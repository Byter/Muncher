package software.visionary.muncher;

import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.Muncher;
import software.visionary.muncher.api.MutableMeal;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;

public enum InteractiveLogMealMicroservice {
    INSTANCE;

    public static void main(final String[] args) {
        final Scanner in = new Scanner(System.in);
        System.out.println("Welcome to Muncher, a Visionary Software Solutions Vision for Meal logging!");
        System.out.println("What is your name?");
        final Name theMuncher = new Name(in.nextLine());
        busyWait(theMuncher, in);
    }

    private static void busyWait(final Name theMuncher, final Scanner in) {
        final Muncher user = new PersistToFileMuncher(theMuncher);
        do {
            printMenu();
            switch (in.nextLine()) {
                case "0" : new LogAMeal(user, in).run(); break;
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
        System.out.println("0 - Log A Meal");
        System.out.println();
    }

    private static class LogAMeal implements Runnable {
        private final Muncher theMuncher;
        private final Scanner scanner;

        LogAMeal(final Muncher user, final Scanner in) {
            theMuncher = Objects.requireNonNull(user);
            scanner = Objects.requireNonNull(in);
        }

        @Override
        public void run() {
            System.out.println("Enter the year-month-day and time the meal started, e.g. 2019-11-05T23:45");
            final LocalDateTime startedAt = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            System.out.println("Enter the year-month-day and time the meal ended, e.g. 2019-11-05T23:59");
            final LocalDateTime endedAt = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            final MutableMeal toLog = new InMemoryMeal(startedAt.toInstant(ZoneOffset.UTC), endedAt.toInstant(ZoneOffset.UTC));
            addFood(toLog);
            theMuncher.store(toLog);
        }

        private void addFood(final MutableMeal toLog) {
            while (true) {
                System.out.println("+ to add the Name of Food you eat in this meal. / to stop");
                switch (scanner.nextLine()) {
                    case "/":
                        return;
                    case "+":
                        final Food f = new InMemoryFood(new Name(scanner.nextLine()));
                        System.out.println("Adding the food " + f);
                        toLog.store(f);
                        break;
                    default:
                        System.out.println("Unsupported option, choose + or /");
                        break;
                }
            }
        }
    }
}
