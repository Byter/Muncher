package software.visionary.muncher.api;

public interface Foods extends Iterable<Food> {
    boolean has(Food food);
}
