package software.visionary.muncher.api;

import software.visionary.api.Event;
import software.visionary.api.Storable;

public interface Meal extends Event {
    Storable<Food> getFoods();
}
