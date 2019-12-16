package software.visionary.api;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class EventsFromOneWeekAgoToNow<T extends Event> implements Consumer<T> {
    private final List<T> found;
    private final Instant oneWeekAgo;


    public EventsFromOneWeekAgoToNow() {
        found = new ArrayList<>();
        oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
    }

    @Override
    public final void accept(final T event) {
        if (oneWeekAgo.isBefore(event.getStartedAt())) {
            found.add(event);
        }
    }

    @Override
    public Consumer<T> andThen(final Consumer<? super T> after) {
        return (thing -> {
            accept(thing);
            if (found.contains(thing)) {
                after.accept(thing);
            }
        });
    }

    public final boolean contains(final T sought) {
        return found.contains(sought);
    }
}
