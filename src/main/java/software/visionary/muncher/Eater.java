package software.visionary.muncher;

import java.util.function.Consumer;

public interface Eater {
    void eat(Food food);
    void ask(Consumer<Food> question);
}
