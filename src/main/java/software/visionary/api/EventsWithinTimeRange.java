package software.visionary.api;

import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

public abstract class EventsWithinTimeRange<T extends Event> implements Consumer<T> {
    private final List<T> found;
    private final Instant start;
    private final Instant end;

    public EventsWithinTimeRange(final Instant start, final Instant end) {
        found = new ArrayList<>();
        this.start = Objects.requireNonNull(start);
        this.end = Objects.requireNonNull(end);
    }

    @Override
    public void accept(final T meal) {
        if (start.isBefore(meal.getStartedAt()) && end.isAfter(meal.getEndedAt())) {
            found.add(meal);
        }
    }

    public boolean contains(final T sought) {
        return found.contains(sought);
    }

    public Optional<T> mostRecent() {
        final List<T> toOrganize = new ArrayList<>(found);
        toOrganize.sort(Comparator.comparing(T::getStartedAt));
        return toOrganize.stream().findFirst();
    }
}
