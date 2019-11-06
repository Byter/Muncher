package software.visionary.muncher;

import software.visionary.muncher.api.Muncher;

import java.util.Objects;

final class RecollectMealsFromLastWeek implements Runnable {
    private final Muncher user;

    RecollectMealsFromLastWeek(final Muncher user) {
        this.user = Objects.requireNonNull(user);
    }

    public void run() {
        final MealsFromOneWeekAgoToNow query = new MealsFromOneWeekAgoToNow();
        user.recollect(query.andThen(System.out::println));
    }
}
