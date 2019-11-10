package software.visionary.muncher.api;

public interface Storable<T> extends Queryable<T> {
    void store(T toStore);
}
