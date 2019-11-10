package software.visionary.muncher;

import software.visionary.api.EventsFromOneWeekAgoToNow;
import software.visionary.api.Name;
import software.visionary.api.Queryable;
import software.visionary.muncher.api.Meal;

final class RecollectMealsFromLastWeek implements Runnable {
    private final Queryable<Meal> user;

    RecollectMealsFromLastWeek(final String[] args) {
        final Name name = new Name(args[0]);
        this.user = new PersistToFileMuncher(name);
    }

    public void run() {
        final EventsFromOneWeekAgoToNow<Meal> query = new EventsFromOneWeekAgoToNow<>(){

        };
        user.query(query.andThen(System.out::println));
    }
}
