package software.visionary.muncher;

import org.junit.jupiter.api.Test;
import software.visionary.api.EventsFromOneWeekAgoToNow;
import software.visionary.api.EventsWithinTimeRange;
import software.visionary.api.Storable;
import software.visionary.muncher.api.Meal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class QueryForMealsForATimeRange {
    @Test
    void canQueryMuncherForMealsWithinTheLastWeek() {
        // Given: A Muncher
        final Storable<Meal> mom = Fixtures.createMuncher();
        // And: that person logs a Meal from 14 days ago
        final Meal first = Fixtures.createMealFromXDaysAgo(14);
        mom.store(first);
        // And: that person logs a Meal from 3 days ago
        final Meal second = Fixtures.createMealFromXDaysAgo(3);
        mom.store(second);
        // And: that person logs a Meal from 2 days ago
        final Meal third = Fixtures.createMealFromXDaysAgo(2);
        mom.store(third);
        // And: that person logs a Meal from yesterday
        final Meal fourth = Fixtures.createMealFromXDaysAgo(1);
        mom.store(fourth);
        // And: A query for meals
        final EventsFromOneWeekAgoToNow<Meal> query = new EventsFromOneWeekAgoToNow<>() {
        };
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
        final Storable<Meal> mom = Fixtures.createMuncher();
        // And: that person logs a Meal from 14 days ago
        final Meal first = Fixtures.createMealFromXDaysAgo(14);
        mom.store(first);
        // And: that person logs a Meal from 3 days ago
        final Meal second = Fixtures.createMealFromXDaysAgo(3);
        mom.store(second);
        // And: that person logs a Meal from 2 days ago
        final Meal third = Fixtures.createMealFromXDaysAgo(2);
        mom.store(third);
        // And: that person logs a Meal from yesterday
        final Meal fourth = Fixtures.createMealFromXDaysAgo(1);
        mom.store(fourth);
        // And: A query for meals
        final Instant startTime = Instant.now().minusSeconds(5); // we just created the fixtures, we want them included
        final EventsWithinTimeRange<Meal> query = new EventsWithinTimeRange<Meal>(startTime.minus(14, ChronoUnit.DAYS),
                startTime.minus(7, ChronoUnit.DAYS)){};
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

}
