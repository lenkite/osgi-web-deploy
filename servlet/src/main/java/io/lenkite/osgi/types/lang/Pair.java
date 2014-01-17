package io.lenkite.osgi.types.lang;


import java.io.Serializable;


public class Pair<T, U> implements Serializable {
    private static final long serialVersionUID = 1;

    private final T first;
    private final U second;

    public Pair(T a, U b) {
        this.first = a;
        this.second = b;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        return Hash.hash(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Pair)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Pair<T, U> other = (Pair<T, U>) obj;

        return Equals.equals(first, other.first) && //
                Equals.equals(second, other.second);
    }

    @Override
    public String toString() {
        return "[" + first + ", " + second + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public static <T, U> Pair<T, U> create(T a, U b) {
        return new Pair<T, U>(a, b);
    }

    @SuppressWarnings("unchecked")
    public static final <T, U> Pair<T, U> nullPair() {
        return (Pair<T, U>) NULL_PAIR;
    }

    @SuppressWarnings("rawtypes")
    public static final Pair NULL_PAIR = new Pair<Object, Object>(null, null);
}
