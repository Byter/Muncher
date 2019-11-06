package software.visionary.muncher;

import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.Meal;
import software.visionary.muncher.api.Muncher;
import software.visionary.muncher.api.MutableMeal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class InMemoryMuncher implements Muncher {
    private final Name name;
    private final List<Meal> consumed;

    InMemoryMuncher(final Name name) {
        this.name = Objects.requireNonNull(name);
        consumed = new ArrayList<>();
    }

    @Override
    public void eat(final Food food) {
        final Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
        final MealsWithinTimeRange anyThingEaten = new MealsWithinTimeRange(oneHourAgo, Instant.now());
        recollect(anyThingEaten);
        anyThingEaten.mostRecent()
                .ifPresentOrElse(meal -> ((MutableMeal) meal).add(food), () -> log(InMemoryMeal.fromFood(food)));
    }

    @Override
    public void ask(final Consumer<Food> question) {
        consumed.stream()
        .map(Meal::getFoods)
        .forEach(foods -> foods.forEach(question));
    }

    @Override
    public void log(final Meal meal) {
        consumed.add(Objects.requireNonNull(meal));
    }

    @Override
    public void recollect(final Consumer<Meal> query) {
        consumed.forEach(query::accept);
    }

    @Override
    public String toString() { return name.toString(); }
}
