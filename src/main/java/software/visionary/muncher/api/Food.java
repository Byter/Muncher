package software.visionary.muncher.api;

import software.visionary.api.Name;

public interface Food {
    default Name getName() {
        return new Name(Food.class.getName());
    }
}
