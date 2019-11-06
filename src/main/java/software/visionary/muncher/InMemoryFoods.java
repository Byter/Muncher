package software.visionary.muncher;

import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.MutableFoods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

final class InMemoryFoods implements MutableFoods {
    private final List<Food> foods;

    InMemoryFoods() {
        foods = new ArrayList<>();
    }

    @Override
    public boolean has(final Food food) {
        return foods.contains(food);
    }

    @Override
    public void add(final Food food) {
        foods.add(Objects.requireNonNull(food));
    }

    @Override
    public Iterator<Food> iterator() {
        return foods.iterator();
    }
}
