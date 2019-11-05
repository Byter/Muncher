package software.visionary.muncher;

import java.time.Instant;

public interface Meal {
    Instant getStartedAt();
    Instant getEndedAt();
    Foods getFoods();
    void add(Food food);
}
