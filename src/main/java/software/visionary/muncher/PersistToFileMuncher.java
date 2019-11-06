package software.visionary.muncher;

import software.visionary.muncher.api.Food;
import software.visionary.muncher.api.Foods;
import software.visionary.muncher.api.Meal;
import software.visionary.muncher.api.Muncher;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

final class PersistToFileMuncher implements Muncher {
    private final Muncher delegate;

    PersistToFileMuncher(final Muncher delegate) {
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
        recollect(previouslyStored);
        try(final ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(stored))) {
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

    static final class VerifyMealsStored implements Consumer<Meal> {
        private final List<Meal> stored;

        VerifyMealsStored() {
            stored = new ArrayList<>();
        }

        @Override
        public void accept(final Meal meal) {
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

    @Override
    public void recollect(final Consumer<Meal> query) {
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
            } catch (final IOException | ClassNotFoundException e) {
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
    }
}
