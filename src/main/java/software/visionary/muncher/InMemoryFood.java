package software.visionary.muncher;

import software.visionary.muncher.api.Food;

import java.util.Objects;

final class InMemoryFood implements Food {
    private final Name name;

    InMemoryFood(final Name name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public Name getName() {
        return name;
    }
}
