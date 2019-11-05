package software.visionary.muncher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;

final class MealsWithinTimeRange implements Consumer<Meal> {
    private final List<Meal> found;
    private final Instant start;
    private final Instant end;

    MealsWithinTimeRange(final Instant start, final Instant end) {
        found = new ArrayList<>();
        this.start = Objects.requireNonNull(start);
        this.end = Objects.requireNonNull(end);
    }

    static MealsWithinTimeRange mealsEatenLastWeek() {
        return new MealsWithinTimeRange(Instant.now().minus(15, ChronoUnit.DAYS), Instant.now().minus(7, ChronoUnit.DAYS));
    }

    @Override
    public void accept(final Meal meal) {
        if (start.isBefore(meal.getStartedAt()) && end.isAfter(meal.getEndedAt())) {
            found.add(meal);
        }
    }

    boolean contains(final Meal sought) {
        return found.contains(sought);
    }

    Optional<Meal> mostRecent() {
        final List<Meal> toOrganize = new ArrayList<>(found);
        toOrganize.sort(Comparator.comparing(Meal::getStartedAt));
        return toOrganize.stream().findFirst();
    }
}
