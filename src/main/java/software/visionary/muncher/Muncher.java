package software.visionary.muncher;

import java.util.function.Consumer;

public interface Muncher extends Eater {
    void log(Meal meal);
    void recollect(Consumer<Meal> query);
}
