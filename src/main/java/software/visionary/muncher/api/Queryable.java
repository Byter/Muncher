package software.visionary.muncher.api;

import java.util.function.Consumer;

public interface Queryable<T> {
    void query(Consumer<T> visitor);
}
