package software.visionary.muncher;

import software.visionary.muncher.api.Muncher;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

final class RecollectMealsFromTimeRange implements Runnable {
    private final Scanner in;
    private final Muncher user;

    RecollectMealsFromTimeRange(final Scanner in, final Muncher user) {
        this.in = in;
        this.user = user;
    }

    public void run() {
        System.out.println("Enter the year-month-day and time the meal started, e.g. 2019-11-05T23:45");
        final LocalDateTime startedAt = LocalDateTime.parse(in.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println("Enter the year-month-day and time the meal ended, e.g. 2019-11-05T23:59");
        final LocalDateTime endedAt = LocalDateTime.parse(in.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        final MealsWithinTimeRange query = new MealsWithinTimeRange(startedAt.toInstant(ZoneOffset.UTC), endedAt.toInstant(ZoneOffset.UTC));
        user.recollect(query.andThen(System.out::println));
    }
}
