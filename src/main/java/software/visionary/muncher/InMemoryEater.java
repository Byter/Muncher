package software.visionary.muncher;

import software.visionary.muncher.api.Eater;
import software.visionary.muncher.api.Food;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class InMemoryEater implements Eater {
    private final List<Food> consumed;

    InMemoryEater() {
        consumed = new ArrayList<>();
    }

    @Override
    public void log(final Food toStore) {
        consumed.add(Objects.requireNonNull(toStore));
    }

    @Override
    public void query(final Consumer<Food> question) {
        consumed.forEach(question::accept);
    }
}
