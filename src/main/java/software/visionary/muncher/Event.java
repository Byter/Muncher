package software.visionary.muncher;

import java.time.Instant;

public interface Event {
    Instant getStartedAt();
    Instant getEndedAt();
}
