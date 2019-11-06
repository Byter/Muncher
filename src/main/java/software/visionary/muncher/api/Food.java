package software.visionary.muncher.api;

import software.visionary.muncher.Name;

public interface Food {
    default Name getName() {
        return new Name(Food.class.getName());
    }
}
