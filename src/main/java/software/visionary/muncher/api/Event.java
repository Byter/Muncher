package software.visionary.muncher.api;

import java.time.Instant;

public interface Event {
    Instant getStartedAt();

    Instant getEndedAt();
}
