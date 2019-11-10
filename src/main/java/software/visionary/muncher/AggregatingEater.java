package software.visionary.muncher;

import software.visionary.api.EventsWithinTimeRange;
import software.visionary.api.Storable;
import software.visionary.muncher.api.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class AggregatingEater implements Storable<Food> {
    private final Storable<Meal> muncher;

    AggregatingEater(final Storable<Meal> muncher) {
        this.muncher = Objects.requireNonNull(muncher);
    }

    @Override
    public void store(final Food toStore) {
        final Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
        final EventsWithinTimeRange<Meal> anyThingEaten = new EventsWithinTimeRange<>(oneHourAgo, Instant.now()) {};
        muncher.query(anyThingEaten);
        anyThingEaten.mostRecent()
                .ifPresentOrElse(meal -> ((MutableMeal) meal).store(toStore), () -> muncher.store(InMemoryMeal.fromFood(toStore)));
    }

    @Override
    public void query(final Consumer<Food> visitor) {
        final List<Storable<Food>> foods = new ArrayList<>();
        muncher.query(m -> foods.add(m.getFoods()));
        foods.forEach(theFoods -> theFoods.query(visitor));
    }
}
