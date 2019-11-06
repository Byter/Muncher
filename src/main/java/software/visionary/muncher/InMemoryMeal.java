package software.visionary.muncher;

import software.visionary.muncher.api.*;

import java.time.Instant;
import java.util.Objects;

final class InMemoryMeal implements MutableMeal {
    private final Instant startTime;
    private final Instant endTime;
    private final MutableFoods foods;

    InMemoryMeal(final Instant startTime, final Instant endTime) {
        this.startTime = Objects.requireNonNull(startTime);
        this.endTime = Objects.requireNonNull(endTime);
        this.foods = new InMemoryFoods();
    }

    static Meal fromFood(final Food food) {
        final InMemoryMeal newMeal = new InMemoryMeal(Instant.now(), Instant.now());
        newMeal.add(food);
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
    public Foods getFoods() {
        return foods;
    }

    @Override
    public void add(final Food food) {
        foods.add(Objects.requireNonNull(food));
    }
}
