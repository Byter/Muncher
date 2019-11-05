package software.visionary.muncher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

final class PersistMealLogAsAFile {
    @Test
    void canSaveMealsToAFile() {
        // Given: A Muncher that persists data to disk
        final Muncher mom = new PersistToFileMuncher(Fixtures.createMuncher());
        // When: that person logs a Meal from 14 days ago
        final Meal first = Fixtures.createMealFromXDaysAgo(14);
        mom.log(first);
        // Then: A file should exist for that muncher
        Assertions.assertTrue(Files.exists(Paths.get(mom.toString())));
    }

    @Test
    void canLoadMealsFromAFile() {
        // Given: A Muncher that persists data to disk
        final Muncher mom = new PersistToFileMuncher(Fixtures.createMuncher());
        // And: that person logs a Meal from 14 days ago
        final Meal first = Fixtures.createMealFromXDaysAgo(14);
        mom.log(first);
        // And: that person logs a Meal from 3 days ago
        final Meal second = Fixtures.createMealFromXDaysAgo(3);
        mom.log(second);
        // And: that person logs a Meal from 2 days ago
        final Meal third = Fixtures.createMealFromXDaysAgo(2);
        mom.log(third);
        // And: that person logs a Meal from yesterday
        final Meal fourth = Fixtures.createMealFromXDaysAgo(1);
        mom.log(fourth);
        // When: I query the Muncher for all Meals stored
        final VerifyMealsStored verifier = new VerifyMealsStored();
        mom.query(verifier);
        // Then: every meal logged should be stored
        Assertions.assertTrue(verifier.hasMeal(first));
        Assertions.assertTrue(verifier.hasMeal(second));
        Assertions.assertTrue(verifier.hasMeal(third));
        Assertions.assertTrue(verifier.hasMeal(fourth));
    }

    private static final class PersistToFileMuncher implements Muncher {
        private final Muncher delegate;

        private PersistToFileMuncher(final Muncher delegate) {
            this.delegate = Objects.requireNonNull(delegate);
        }

        @Override
        public void eat(final Food food) {
            // TODO: unify Meal and Food
            delegate.eat(food);
        }

        @Override
        public void ask(final Consumer<Food> question) {
            // TODO: unify Meal and Food
            delegate.ask(question);
        }

        @Override
        public synchronized void log(final Meal meal) {
            final File stored = getFileToSaveAs();
            final VerifyMealsStored previouslyStored = new VerifyMealsStored();
            query(previouslyStored);
            try(final ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(stored));) {
                for (final Meal storeAgain : previouslyStored.getStoredMeals()) {
                    os.writeObject(new SerializedMeal(storeAgain));
                }
                os.writeObject(new SerializedMeal(meal));
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }

        private File getFileToSaveAs() {
            final File stored = Paths.get(toString()).toFile();
            if (!stored.exists()) {
                try {
                    Files.createFile(stored.toPath());
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return stored;
        }

        @Override
        public void query(final Consumer<Meal> query) {
            final List<Meal> saved = new ArrayList<>();
            final File stored = getFileToSaveAs();
            if (stored.length() != 0) {
                try(final ObjectInputStream is = new ObjectInputStream(new FileInputStream(stored))) {
                    while (true) {
                        try {
                            final SerializedMeal aMeal = (SerializedMeal) is.readObject();
                            saved.add(aMeal);
                        } catch (final EOFException e) {
                            break; // we've read all the objects. what a shitty API.
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            saved.forEach(query);
        }

        private static final class SerializedMeal implements Serializable, Meal {
            private final Instant start, end;

            private SerializedMeal(final Meal meal) {
                start = meal.getStartedAt();
                end = meal.getEndedAt();
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
            public Foods getFoods() {
                // TODO: make Foods serializable and flesh this out
                return null;
            }

            @Override
            public void add(Food food) {
                // TODO: make Foods serializable and flesh this out
            }
        }
    }

    private static class VerifyMealsStored implements Consumer<Meal> {
        private final List<Meal> stored;

        VerifyMealsStored() {
             stored = new ArrayList<>();
        }

        @Override
        public void accept(Meal meal) {
            stored.add(meal);
        }

        boolean hasMeal(final Meal toFind) {
            final Optional<Meal> searched = stored.parallelStream()
                    .filter(m -> m.getStartedAt().equals(toFind.getStartedAt()) && m.getEndedAt().equals(toFind.getEndedAt()))
                    .findFirst();
            return searched.isPresent();
        }

        List<Meal> getStoredMeals() { return new ArrayList<>(stored); }
    }
}
