package software.visionary.muncher;

import software.visionary.api.Name;

import java.util.Scanner;

public enum InteractiveRecollectMealsMicroservice {
    INSTANCE;

    public static void main(final String[] args) {
        final Scanner in = new Scanner(System.in);
        System.out.println("Welcome to Muncher, a Visionary Software Solutions Vision for Meal logging!");
        System.out.println("What is your name?");
        final Name theMuncher = new Name(in.nextLine());
        busyWait(theMuncher, in);
    }

    private static void busyWait(final Name theMuncher, final Scanner in) {
        do {
            printMenu();
            switch (in.nextLine()) {
                case "0" : new RecollectMealsFromLastWeek(new String[] { theMuncher.toString()}).run(); break;
                case "1" : new RecollectMealsFromTimeRange(new String[]{in.nextLine(), in.nextLine(), theMuncher.toString()}).run(); break;
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
}
