package software.visionary.muncher;

public interface Meal extends Event {
    Foods getFoods();
    void add(Food food);
}
