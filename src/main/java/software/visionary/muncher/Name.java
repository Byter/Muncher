package software.visionary.muncher;

import java.util.Objects;

public final class Name implements CharSequence {
    private final String source;

    public Name(final String source) {
        this.source = Objects.requireNonNull(source);
        if (this.source.trim().isEmpty()) {
            throw new IllegalArgumentException("Can't have a blank name!");
        }
    }

    @Override
    public int length() {
        return source.length();
    }

    @Override
    public char charAt(final int index) {
        return source.charAt(index);
    }

    @Override
    public CharSequence subSequence(final int beginIndex, final int endIndex) {
        return source.subSequence(beginIndex, endIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source);
    }

    @Override
    public boolean equals(final Object that) {
        return this == that || ((that != null) && that.getClass().isAssignableFrom(Name.class) && this.source.equals(((Name)that).source));
    }

    @Override
    public String toString() { return source; }
}
