package software.visionary.muncher.api;

import software.visionary.api.Event;
import software.visionary.api.Queryable;

public interface Meal extends Event {
    Queryable<Food> getFoods();
}
