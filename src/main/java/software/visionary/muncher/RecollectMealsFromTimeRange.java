package software.visionary.muncher;

import software.visionary.muncher.api.Muncher;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

final class RecollectMealsFromTimeRange implements Runnable {
    private final String startTime, endTime;
    private final Muncher user;

    RecollectMealsFromTimeRange(final Scanner in, final Muncher user) {
        this(in.nextLine(), in.nextLine(), user);
    }

    RecollectMealsFromTimeRange(final String startTime, final String endTime, final Muncher user) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
    }

    public void run() {
        System.out.println("Enter the year-month-day and time the meal started, e.g. 2019-11-05T23:45");
        final LocalDateTime startedAt = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println("Enter the year-month-day and time the meal ended, e.g. 2019-11-05T23:59");
        final LocalDateTime endedAt = LocalDateTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        final MealsWithinTimeRange query = new MealsWithinTimeRange(startedAt.toInstant(ZoneOffset.UTC), endedAt.toInstant(ZoneOffset.UTC));
        user.recollect(query.andThen(System.out::println));
    }
}
