package software.visionary.muncher;

import software.visionary.muncher.api.Meal;
import software.visionary.muncher.api.Muncher;

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
    public void log(final Meal toStore) {
        consumed.add(Objects.requireNonNull(toStore));
    }

    @Override
    public void query(final Consumer<Meal> query) {
        consumed.forEach(query::accept);
    }

    @Override
    public String toString() { return name.toString(); }
}
