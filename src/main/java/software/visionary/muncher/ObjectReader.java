package software.visionary.muncher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

enum ObjectReader {
    INSTANCE;

    static List<Object> readAllObjects(final File stored) {
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
}