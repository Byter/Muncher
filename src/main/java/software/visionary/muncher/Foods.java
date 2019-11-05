package software.visionary.muncher;

public interface Foods extends Iterable<Food> {
    boolean has(final Food food);
}
