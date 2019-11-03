package software.visionary.muncher;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueryForMealsForATimeRange {
    @Test
    void canQueryMuncherForMealsWithinTheLastWeek() {
        // Given: A Muncher
        final Muncher mom = Fixtures.createMuncher();
        // And: that person logs a Meal from 14 days ago
        final Meal first = Fixtures.createMealFromXDaysAgo(14);
        mom.log(first);
        // And: that person logs a Meal from 3 days ago
        final Meal second = Fixtures.createMealFromXDaysAgo(3);
        mom.log(second);
        // And: that person logs a Meal from 2 days ago
        final Meal third = Fixtures.createMealFromXDaysAgo(2);
        mom.log(third);
        // And: that person logs a Meal from yesterday
        final Meal fourth = Fixtures.createMealFromXDaysAgo(1);
        mom.log(fourth);
        // And: A query for meals
        final MealsFromOneWeekAgoToNow query = new MealsFromOneWeekAgoToNow();
        // When: I query
        mom.query(query);
        // Then: the fourth weight is stored
        assertTrue(query.contains(fourth));
        // And: the third weight is stored
        assertTrue(query.contains(third));
        // And: the second weight is stored
        assertTrue(query.contains(second));
        // And: the first weight is not stored
        assertFalse(query.contains(first));
    }

    @Test
    void canQueryMuncherForMealsTwoWeeksAgo() {
        // Given: A Muncher
        final Muncher mom = Fixtures.createMuncher();
        // And: that person logs a Meal from 14 days ago
        final Meal first = Fixtures.createMealFromXDaysAgo(14);
        mom.log(first);
        // And: that person logs a Meal from 3 days ago
        final Meal second = Fixtures.createMealFromXDaysAgo(3);
        mom.log(second);
        // And: that person logs a Meal from 2 days ago
        final Meal third = Fixtures.createMealFromXDaysAgo(2);
        mom.log(third);
        // And: that person logs a Meal from yesterday
        final Meal fourth = Fixtures.createMealFromXDaysAgo(1);
        mom.log(fourth);
        // And: A query for meals
        final MealsWithinTimeRange query = MealsWithinTimeRange.mealsEatenLastWeek();
        // When: I query
        mom.query(query);
        // Then: the fourth weight is not stored
        assertFalse(query.contains(fourth));
        // And: the third weight is not stored
        assertFalse(query.contains(third));
        // And: the second weight is not stored
        assertFalse(query.contains(second));
        // And: the first weight is stored
        assertTrue(query.contains(first));
    }

    private static final class MealsFromOneWeekAgoToNow implements Consumer<Meal> {
        private final List<Meal> found;
        private final Instant oneWeekAgo;

        MealsFromOneWeekAgoToNow() {
            found = new ArrayList<>();
            oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
        }

        @Override
        public void accept(final Meal meal) {
            if (oneWeekAgo.isBefore(meal.getStartedAt())) {
                found.add(meal);
            }
        }

        boolean contains(final Meal sought) {
            return found.contains(sought);
        }
    }

    private static final class MealsWithinTimeRange implements Consumer<Meal> {
        private final List<Meal> found;
        private final Instant start;
        private final Instant end;

        MealsWithinTimeRange(final Instant start, final Instant end) {
            found = new ArrayList<>();
            this.start = Objects.requireNonNull(start);
            this.end = Objects.requireNonNull(end);
        }

        private static MealsWithinTimeRange mealsEatenLastWeek() {
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
    }
}
