package software.visionary.muncher;

import software.visionary.api.Queryable;
import software.visionary.muncher.api.Meal;

final class RecollectMealsFromLastWeek implements Runnable {
    private final Queryable<Meal> user;

    RecollectMealsFromLastWeek(final String[] args) {
        final Name name = new Name(args[0]);
        this.user = new PersistToFileMuncher(name);
    }

    public void run() {
        final MealsFromOneWeekAgoToNow query = new MealsFromOneWeekAgoToNow();
        user.query(query.andThen(System.out::println));
    }
}
