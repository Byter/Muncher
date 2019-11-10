package software.visionary.muncher;

import software.visionary.api.Storable;
import software.visionary.muncher.api.Food;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class InMemoryEater implements Storable<Food> {
    private final List<Food> consumed;

    InMemoryEater() {
        consumed = new ArrayList<>();
    }

    @Override
    public void store(final Food toStore) {
        consumed.add(Objects.requireNonNull(toStore));
    }

    @Override
    public void query(final Consumer<Food> visitor) {
        consumed.forEach(visitor::accept);
    }
}
