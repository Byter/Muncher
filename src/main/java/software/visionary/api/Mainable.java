package software.visionary.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Objects;

public abstract class Mainable implements Runnable {
    private final Deque<String> args;
    private final InputStream input;
    private final OutputStream output;

    public Mainable(final String[] args, final InputStream in, final OutputStream out) {
        this.args = new ArrayDeque<>(Arrays.asList(args == null ? new String[0] : args));
        input = Objects.requireNonNull(in);
        output = Objects.requireNonNull(out);
    }

    protected Deque<String> getArgs() { return args; }

    protected void writeToOutput(final String toWrite) {
        try {
            output.write(String.format("%s%n",toWrite).getBytes(StandardCharsets.UTF_8));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void printHelp();
    
    @Override
    public final void run() {
        if (getArgs().size() == 0 || getArgs().contains("help")) {
            printHelp();
            return;
        }
        execute();
    }

    protected abstract void execute();
}
