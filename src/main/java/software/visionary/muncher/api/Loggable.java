package software.visionary.muncher.api;

public interface Loggable<T> extends Queryable<T> {
    void log(T toStore);
}
