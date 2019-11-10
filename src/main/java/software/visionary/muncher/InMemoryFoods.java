package software.visionary.muncher;

import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.Storable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class InMemoryFoods implements Storable<Food> {
    private final List<Food> foods;

    InMemoryFoods() {
        foods = new ArrayList<>();
    }

    @Override
    public void store(final Food toStore) {
        foods.add(Objects.requireNonNull(toStore));
    }

    @Override
    public void query(final Consumer<Food> visitor) {
        foods.forEach(visitor::accept);
    }
}
