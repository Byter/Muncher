package software.visionary.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

final class EventsFromOneWeekAgoToNowTest {
    private static class TooLongAgo implements Event {

        @Override
        public Instant getStartedAt() {
            return Instant.now().minus(7, ChronoUnit.DAYS).minus(1, ChronoUnit.MINUTES);
        }

        @Override
        public Instant getEndedAt() {
            return Instant.now();
        }
    }

    private static class NotYet implements Event {

        @Override
        public Instant getStartedAt() {
            return Instant.now().minus(7, ChronoUnit.DAYS).minus(1, ChronoUnit.MINUTES);
        }

        @Override
        public Instant getEndedAt() {
            return Instant.now().plus(5, ChronoUnit.MINUTES);
        }
    }

    private static class InRange implements Event {

        @Override
        public Instant getStartedAt() {
            return Instant.now().minus(7, ChronoUnit.DAYS).plus(1, ChronoUnit.MINUTES);
        }

        @Override
        public Instant getEndedAt() {
            return Instant.now().minus(1, ChronoUnit.DAYS);
        }
    }

    @Test
    void canFindEventsWithinLastWeek() {
        final Event before = new TooLongAgo();
        final EventsFromOneWeekAgoToNow toTest = new EventsFromOneWeekAgoToNow() {
            @Override
            public void accept(final Object o) {
                if (o instanceof Event) {
                    super.accept(((Event) o));
                }
            }
        };
        toTest.accept(before);
        Assertions.assertFalse(toTest.contains(before));
        final Event after = new NotYet();
        toTest.accept(after);
        Assertions.assertFalse(toTest.contains(after));
        final Event inRange = new InRange();
        toTest.accept(inRange);
        Assertions.assertTrue(toTest.contains(inRange));
    }
}
