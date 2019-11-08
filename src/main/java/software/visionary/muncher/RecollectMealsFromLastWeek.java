package software.visionary.muncher;

import software.visionary.muncher.api.Muncher;

final class RecollectMealsFromLastWeek implements Runnable {
    private final Muncher user;

    RecollectMealsFromLastWeek(final String[] args) {
        final Name name = new Name(args[1]);
        this.user = new PersistToFileMuncher(name);
    }

    public void run() {
        final MealsFromOneWeekAgoToNow query = new MealsFromOneWeekAgoToNow();
        user.recollect(query.andThen(System.out::println));
    }
}
