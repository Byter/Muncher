package software.visionary.muncher;

import software.visionary.muncher.api.Muncher;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

final class RecollectMealsFromTimeRange implements Runnable {
    private final String startTime, endTime;
    private final Muncher user;

    RecollectMealsFromTimeRange(final String[] args) {
        final Deque<String> toProcess = new ArrayDeque<>(Arrays.asList(args));
        final Name theMuncher = new Name(toProcess.pop());
        final PersistToFileMuncher user = new PersistToFileMuncher(theMuncher);
        this.startTime = toProcess.pop();
        this.endTime = toProcess.pop();
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
