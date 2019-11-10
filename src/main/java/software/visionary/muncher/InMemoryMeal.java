package software.visionary.muncher;

import software.visionary.muncher.api.*;

import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;

final class InMemoryMeal implements MutableMeal {
    private final Instant startTime;
    private final Instant endTime;
    private final Storable<Food> foods;

    InMemoryMeal(final Instant startTime, final Instant endTime) {
        this.startTime = Objects.requireNonNull(startTime);
        this.endTime = Objects.requireNonNull(endTime);
        this.foods = new InMemoryFoods();
    }

    static Meal fromFood(final Food food) {
        final InMemoryMeal newMeal = new InMemoryMeal(Instant.now(), Instant.now());
        newMeal.store(food);
        return newMeal;
    }

    @Override
    public Instant getStartedAt() {
        return startTime;
    }

    @Override
    public Instant getEndedAt() {
        return endTime;
    }

    @Override
    public Storable<Food> getFoods() {
        return foods;
    }

    @Override
    public void store(final Food food) {
        foods.store(Objects.requireNonNull(food));
    }

    @Override
    public void query(final Consumer<Food> visitor) {
        foods.query(visitor::accept);
    }
}
