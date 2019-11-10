package software.visionary.muncher;

import software.visionary.api.EventsWithinTimeRange;
import software.visionary.muncher.api.Meal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

final class MealsWithinTimeRange extends EventsWithinTimeRange<Meal> {

    MealsWithinTimeRange(final Instant start, final Instant end) {
        super(start, end);
    }

    static MealsWithinTimeRange mealsEatenLastWeek() {
        return new MealsWithinTimeRange(Instant.now().minus(15, ChronoUnit.DAYS), Instant.now().minus(7, ChronoUnit.DAYS));
    }
}
