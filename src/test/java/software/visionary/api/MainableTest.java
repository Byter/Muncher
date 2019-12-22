package software.visionary.api;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Deque;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

final class MainableTest {
    private static final class TestDouble extends Mainable {
        private static final String HELP_MESSAGE = "Some help";
        private static final String EXECUTE_MESSAGE = "Executed";
        public TestDouble(final String[] args, final InputStream in, final OutputStream out) {
            super(args, in, out);
        }

        @Override
        protected void printHelp() {
            writeToOutput(HELP_MESSAGE);
        }

        @Override
        protected void execute() {
            writeToOutput(EXECUTE_MESSAGE);
        }
    }

    @Test
    void nullArgsTolerant() {
        final Mainable toTest = new TestDouble(null, System.in, System.out);
        final Deque<String> arguments = toTest.getArgs();
        assertTrue(arguments.isEmpty());
    }

    @Test
    void rejectsNullInputStream() {
        assertThrows(NullPointerException.class, () -> new TestDouble(new String[0], null, System.out));
    }

    @Test
    void rejectsNullOutputStream() {
        assertThrows(NullPointerException.class, () -> new TestDouble(new String[0], System.in, null));
    }

    @Test
    void canWriteStringToOutputStream() {
        final String toWrite = UUID.randomUUID().toString();
        final OutputStream out = new ByteArrayOutputStream(toWrite.length());
        final Mainable toTest = new TestDouble(new String[0], System.in, out);
        toTest.writeToOutput(toWrite);
        assertEquals(String.format("%s%n", toWrite), out.toString());
    }

    @Test
    void throwsRuntimeExceptionWhenWritingToExceptionalOutputStream() throws IOException {
        final String toWrite = UUID.randomUUID().toString();
        final OutputStream out = new OutputStream() {
            @Override
            public void write(final int i) throws IOException {
                throw new IOException("except");
            }
        };
        out.close();
        final Mainable toTest = new TestDouble(new String[0], System.in, out);
        assertThrows(RuntimeException.class, () ->toTest.writeToOutput(toWrite));
    }

    @Test
    void printsHelpWhenEmptyArgsOnRun() {
        final OutputStream out = new ByteArrayOutputStream(10);
        final Mainable toTest = new TestDouble(new String[0], System.in, out);
        toTest.run();
        assertTrue(out.toString().contains(TestDouble.HELP_MESSAGE));
        assertFalse(out.toString().contains(TestDouble.EXECUTE_MESSAGE));
    }

    @Test
    void printsHelpWhenHelpInArgsOnRun() {
        final OutputStream out = new ByteArrayOutputStream(10);
        final Mainable toTest = new TestDouble(new String[]{"help"}, System.in, out);
        toTest.run();
        assertTrue(out.toString().contains(TestDouble.HELP_MESSAGE));
        assertFalse(out.toString().contains(TestDouble.EXECUTE_MESSAGE));
    }

    @Test
    void executesWhenValid() {
        final OutputStream out = new ByteArrayOutputStream(10);
        final Mainable toTest = new TestDouble(new String[]{"something"}, System.in, out);
        toTest.run();
        assertFalse(out.toString().contains(TestDouble.HELP_MESSAGE));
        assertTrue(out.toString().contains(TestDouble.EXECUTE_MESSAGE));
    }
}
