package software.visionary.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

final class EventsFromWithinTimeRangeTest {
    private static class TooLongAgo implements Event {

        @Override
        public Instant getStartedAt() {
            return Instant.now().minusSeconds(6);
        }

        @Override
        public Instant getEndedAt() {
            return Instant.now().minusSeconds(4);
        }
    }

    private static class NotYet implements Event {

        @Override
        public Instant getStartedAt() {
            return Instant.now().minusSeconds(4);
        }

        @Override
        public Instant getEndedAt() {
            return Instant.now().plusSeconds(1);
        }
    }

    private static class InRange implements Event {

        @Override
        public Instant getStartedAt() {
            return Instant.now().minusSeconds(4);
        }

        @Override
        public Instant getEndedAt() {
            return Instant.now().minusSeconds(3);
        }
    }

    @Test
    void canFindEventsWithinTimeRage() {
        final Instant startTime = Instant.now().minusSeconds(5);
        final Instant endTime = Instant.now().minusSeconds(2);
        final Event before = new TooLongAgo();
        final EventsWithinTimeRange toTest = new EventsWithinTimeRange(startTime, endTime) {
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

    @Test
    void canFindMostRecentEvent() {
        final Instant startTime = Instant.now().minusSeconds(5);
        final Instant endTime = Instant.now().minusSeconds(2);
        final EventsWithinTimeRange toTest = new EventsWithinTimeRange(startTime, endTime) {
            @Override
            public void accept(final Object o) {
                if (o instanceof Event) {
                    super.accept(((Event) o));
                }
            }
        };

        final Event inRange = new InRange();
        toTest.accept(inRange);

        final Event mostRecent = new Event() {
            @Override
            public Instant getStartedAt() {
                return Instant.now().minusSeconds(4);
            }

            @Override
            public Instant getEndedAt() {
                return Instant.now().minusSeconds(3);
            }
        };
        toTest.accept(mostRecent);
        Assertions.assertTrue(toTest.mostRecent().isPresent());
        Assertions.assertEquals(mostRecent, toTest.mostRecent().get());
    }
}
