package software.visionary.muncher;

import software.visionary.muncher.api.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class AggregatingEater implements Eater {
    private final Muncher muncher;

    AggregatingEater(final Muncher muncher) {
        this.muncher = Objects.requireNonNull(muncher);
    }

    @Override
    public void store(final Food toStore) {
        final Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
        final MealsWithinTimeRange anyThingEaten = new MealsWithinTimeRange(oneHourAgo, Instant.now());
        muncher.query(anyThingEaten);
        anyThingEaten.mostRecent()
                .ifPresentOrElse(meal -> ((MutableMeal) meal).store(toStore), () -> muncher.store(InMemoryMeal.fromFood(toStore)));
    }

    @Override
    public void query(final Consumer<Food> visitor) {
        final List<Foods> foods = new ArrayList<>();
        muncher.query(m -> foods.add(m.getFoods()));
        foods.forEach(theFoods -> theFoods.query(visitor));
    }
}
