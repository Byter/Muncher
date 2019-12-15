package software.visionary.muncher;

import software.visionary.api.EventsWithinTimeRange;
import software.visionary.api.Storable;
import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.Meal;
import software.visionary.muncher.api.MutableMeal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.function.Consumer;

final class AggregatingEater implements Storable<Food> {
    private final Storable<Meal> muncher;
    private final Instant startTime;

    AggregatingEater(final Storable<Meal> muncher) {
        this(muncher, Instant.now().minus(1, ChronoUnit.HOURS));
    }

    AggregatingEater(final Storable<Meal> muncher, final Instant start) {
        this.muncher = Objects.requireNonNull(muncher);
        startTime = Objects.requireNonNull(start);
    }

    @Override
    public void store(final Food toStore) {
        final EventsWithinTimeRange<Meal> anyThingEaten = new EventsWithinTimeRange<>(startTime, Instant.now()) {};
        muncher.query(anyThingEaten);
        if (anyThingEaten.mostRecent().isPresent()) {
            ((MutableMeal) anyThingEaten.mostRecent().get()).store(toStore);
        } else {
            muncher.store(InMemoryMeal.fromFood(toStore));
        }
    }

    @Override
    public void query(final Consumer<Food> visitor) {
        muncher.query(m -> m.getFoods().query(visitor));
    }
}
