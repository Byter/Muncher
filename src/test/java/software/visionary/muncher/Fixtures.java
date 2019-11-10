package software.visionary.muncher;

import software.visionary.api.Name;
import software.visionary.api.Storable;
import software.visionary.muncher.api.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

final class Fixtures {
    private Fixtures() {}

    static Meal createMealFromXDaysAgo(final int daysAgo) {
        final Instant startTime = Instant.now().minus(daysAgo, ChronoUnit.DAYS);
        final Instant endTime = startTime.plus(1, ChronoUnit.HOURS);
        return new InMemoryMeal(startTime, endTime);
    }

    static Storable<Food> createEater() { return new InMemoryEater();}

    static Storable<Meal> createMuncher() {
        return new InMemoryMuncher(new Name("fakeMuncher"));
    }

    static Storable<Meal> namedMuncher(final Name name) {
        return new InMemoryMuncher(name);
    }

    private static final class BoneBroth implements Food {

    }

    static Food createBoneBroth() {
        return new BoneBroth();
    }
}
