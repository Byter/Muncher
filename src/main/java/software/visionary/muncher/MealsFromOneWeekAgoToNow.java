package software.visionary.muncher;

import software.visionary.muncher.api.Meal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

final class MealsFromOneWeekAgoToNow implements Consumer<Meal> {
    private final List<Meal> found;
    private final Instant oneWeekAgo;

    MealsFromOneWeekAgoToNow() {
        found = new ArrayList<>();
        oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
    }

    @Override
    public void accept(final Meal meal) {
        if (oneWeekAgo.isBefore(meal.getStartedAt())) {
            found.add(meal);
        }
    }

    boolean contains(final Meal sought) {
        return found.contains(sought);
    }

}
