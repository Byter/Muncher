package software.visionary.muncher;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

enum ObjectSerializer {
    INSTANCE;

    static File getFileToSaveAs(final String fileName) {
        final File stored = Paths.get(fileName).toFile();
        if (!stored.exists()) {
            try {
                Files.createFile(stored.toPath());
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        return stored;
    }

    List<Object> readAllObjects(final File stored) {
        final List<Object> objects = new ArrayList<Object>();
        if (stored != null && stored.length() != 0) {
            try (final ObjectInputStream is = new ObjectInputStream(new FileInputStream(stored))) {
                while (true) {
                    try {
                        objects.add(is.readObject());
                    } catch (final EOFException e) {
                        break; // we've read all the objects. what a shitty API.
                    }
                }
            } catch (final IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return objects;
    }

    void writeAllObjectsToFile(final List<?> toWrite, final File stored) {
        try(final ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(stored))) {
            for (final Object o : toWrite) {
                os.writeObject(o);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}