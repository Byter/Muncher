package software.visionary.muncher.api;

public interface Meal extends Event {
    Storable<Food> getFoods();
}
