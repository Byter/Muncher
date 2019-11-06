package software.visionary.muncher.api;

import java.util.function.Consumer;

public interface Eater {
    void eat(Food food);

    void ask(Consumer<Food> question);
}
