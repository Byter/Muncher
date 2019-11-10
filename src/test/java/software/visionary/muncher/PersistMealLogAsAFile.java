package software.visionary.muncher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.Foods;
import software.visionary.muncher.api.Meal;
import software.visionary.muncher.api.Muncher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class PersistMealLogAsAFile {
    @Test
    void canSaveMealsToAFile() {
        // Given: A Muncher that persists data to disk
        final Muncher mom = new PersistToFileMuncher(new Name("Mom"));
        // When: that person logs a Meal from 14 days ago
        final Meal first = Fixtures.createMealFromXDaysAgo(14);
        mom.store(first);
        // Then: A file should exist for that muncher
        Assertions.assertTrue(Files.exists(Paths.get(mom.toString())));
    }

    @Test
    void canLoadMealsFromAFile() {
        // Given: A Muncher that persists data to disk
        final Muncher mom = new PersistToFileMuncher(new Name("Mom"));
        // And: that person logs a Meal from 14 days ago
        final Meal first = Fixtures.createMealFromXDaysAgo(14);
        mom.store(first);
        // And: that person logs a Meal from 3 days ago
        final Meal second = Fixtures.createMealFromXDaysAgo(3);
        mom.store(second);
        // And: that person logs a Meal from 2 days ago
        final Meal third = Fixtures.createMealFromXDaysAgo(2);
        mom.store(third);
        // And: that person logs a Meal from yesterday
        final Meal fourth = Fixtures.createMealFromXDaysAgo(1);
        mom.store(fourth);
        // When: I query the Muncher for all Meals stored
        final List<Meal> sought = new ArrayList<>(4);
        mom.query(meal -> sought.add(new SoughtMeal(meal)));
        // Then: every meal logged should be stored
        Assertions.assertTrue(sought.containsAll(Stream.of(first,second,third,fourth).map(SoughtMeal::new).collect(Collectors.toList())));
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get("Mom"));
    }

    private static final class SoughtMeal implements Meal {
        private final Instant start, end;
        private final List<Food> foods;

        SoughtMeal(final Meal meal) {
            this.start = meal.getStartedAt();
            this.end = meal.getEndedAt();
            foods = new ArrayList<>();
            meal.getFoods().query(foods::add);
        }

        @Override
        public Foods getFoods() {
            return new Foods() {

                @Override
                public void query(final Consumer<Food> visitor) {
                    foods.forEach(visitor::accept);
                }

                @Override
                public void store(final Food toStore) {
                    foods.add(toStore);
                }
            };
        }

        @Override
        public Instant getStartedAt() {
            return start;
        }

        @Override
        public Instant getEndedAt() {
            return end;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final SoughtMeal that = (SoughtMeal) o;
            return start.equals(that.start) &&
                    end.equals(that.end) &&
                    foods.equals(that.foods);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end, foods);
        }
    }
}
