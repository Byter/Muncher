package software.visionary.muncher;

import software.visionary.api.Name;
import software.visionary.api.Storable;
import software.visionary.muncher.api.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class InMemoryMuncher implements Storable<Meal> {
    private final Name name;
    private final List<Meal> consumed;

    InMemoryMuncher(final Name name) {
        this.name = Objects.requireNonNull(name);
        consumed = new ArrayList<>();
    }

    @Override
    public void store(final Meal toStore) {
        consumed.add(Objects.requireNonNull(toStore));
    }

    @Override
    public void query(final Consumer<Meal> visitor) {
        consumed.forEach(visitor::accept);
    }

    @Override
    public String toString() { return name.toString(); }
}
