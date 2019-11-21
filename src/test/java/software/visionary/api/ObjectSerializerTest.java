package software.visionary.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

final class ObjectSerializerTest {
    @Test
    void canFindExistingFileToSaveAs() throws IOException {
        // Given: A path to an existing file to save as as a string
        final Path tempo = Files.createTempFile("fake", "thing");
        // When: I call getFileToSaveAs
        final File result = ObjectSerializer.getFileToSaveAs(tempo.getFileName().toString());
        // Then: The returned file exists and has the path I gave
        Assertions.assertTrue(result.exists());
        Assertions.assertEquals(tempo.getFileName().getFileName().toString(), result.getName());
        Files.deleteIfExists(result.toPath());
    }

    @Test
    void createsFileToSaveAs() throws IOException {
        // Given: A path to a non-existing file to save as as a string
        final Path fakeFile = Paths.get(System.getProperty("java.io.tmpdir"), "woo");
        Assertions.assertFalse(fakeFile.toFile().exists());
        // When: I call getFileToSaveAs
        final File result = ObjectSerializer.getFileToSaveAs(fakeFile.getFileName().toString());
        // Then: The returned file exists and has the path I gave
        Assertions.assertTrue(result.exists());
        Assertions.assertEquals(fakeFile.getFileName().getFileName().toString(), result.getName());
        Files.deleteIfExists(result.toPath());
    }

    @Test
    void throwsExceptionWhenParentDirectoryDoesNotExist() throws IOException {
        // Given: A path to a non-existing file to save as as a string with non-existant parent
        final Path fakeFile = Paths.get("/boosh", "woo");
        Assertions.assertFalse(fakeFile.toFile().exists());
        // When: I call getFileToSaveAs then an exception is thrown
        Assertions.assertThrows(RuntimeException.class, () -> ObjectSerializer.getFileToSaveAs(fakeFile.toAbsolutePath().toString()));
        Files.deleteIfExists(fakeFile);
    }

    @Test
    void canWriteObjectsToAFile() throws IOException {
        // Given: A path to a non-existing file to save
        final Path fakeFile = Paths.get("writtenObjects");
        Assertions.assertFalse(fakeFile.toFile().exists());
        // And: A String to write
        final String toWrite = "a love letter";
        // And: A number to write
        final Integer write2 = 2;
        // And: I call getFileToSaveAs to create a new file
        final File result = ObjectSerializer.getFileToSaveAs(fakeFile.toString());
        Assertions.assertEquals(0, result.length());
        // When: I write the objects to the file
        ObjectSerializer.INSTANCE.writeAllObjectsToFile(Arrays.asList(toWrite, write2), result);
        // Then: The file's length changes (I could say "the file contains those objects", but that forces RW in one test)
        Assertions.assertNotEquals(0, result.length());
        Files.deleteIfExists(fakeFile);
    }

    @Test
    void throwsExceptionWhenInvalidObjectToWrite() throws IOException {
        // Given: A path to a non-existing file to save
        final Path fakeFile = Paths.get("exceptObjects");
        Assertions.assertFalse(fakeFile.toFile().exists());
        // And: An Invalid Object
        final Object toWrite = new Object();
        // And: I call getFileToSaveAs to create a new file
        final File result = ObjectSerializer.getFileToSaveAs(fakeFile.toString());
        Assertions.assertEquals(0, result.length());
        // When: I write the objects to the file
        Assertions.assertThrows(RuntimeException.class, () -> ObjectSerializer.INSTANCE.writeAllObjectsToFile(Arrays.asList(toWrite), result));
        Files.deleteIfExists(fakeFile);
    }

    @Test
    void canReadAllObjectsFromFile() throws IOException {
        // Given: A path to a non-existing file to save
        final Path fakeFile = Paths.get("readObjects");
        Assertions.assertFalse(fakeFile.toFile().exists());
        // And: A String to write
        final String toWrite = "a love letter";
        // And: A number to write
        final Integer write2 = 2;
        // And: I call getFileToSaveAs to create a new file
        final File result = ObjectSerializer.getFileToSaveAs(fakeFile.toString());
        Assertions.assertEquals(0, result.length());
        // And: I write the objects to the file
        ObjectSerializer.INSTANCE.writeAllObjectsToFile(Arrays.asList(toWrite, write2), result);
        // When: I read the objects from the file
        final List<Object> stored = ObjectSerializer.INSTANCE.readAllObjects(result);
        // Then: the objects are stored
        Assertions.assertTrue(stored.contains(toWrite));
        Assertions.assertTrue(stored.contains(write2));
        Files.deleteIfExists(fakeFile);
    }

    @Test
    void throwsExceptionWhenReadingInvalidObjectsFromFile() throws IOException {
        // Given: A path to a non-existing file to save
        final Path fakeFile = Paths.get("dirtyObjects");
        Assertions.assertFalse(fakeFile.toFile().exists());
        // And: I call getFileToSaveAs to create a new file
        final File result = ObjectSerializer.getFileToSaveAs(fakeFile.toString());
        Assertions.assertEquals(0, result.length());
        // And: I write a primitive to the file
        final short shorty = 22;
        final ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fakeFile.toFile()));
        os.writeShort(shorty);
        os.close();
        // When: I read the objects from the file
        Assertions.assertThrows(RuntimeException.class, () -> ObjectSerializer.INSTANCE.readAllObjects(result));
        Files.deleteIfExists(fakeFile);
    }
}
