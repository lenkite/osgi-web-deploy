package io.lenkite.osgi.types.lang;

/**
 * @author Tarun Elankath
 */
public class Triple<T, U, V> {
    @SuppressWarnings("rawtypes")
    public static final Pair NULL_PAIR = new Pair<Object, Object>(null, null);
    private final T first;
    private final U second;
    private final V third;

    public Triple(T a, U b, V c) {
        this.first = a;
        this.second = b;
        this.third = c;
    }

    public static <T, U> Pair<T, U> create(T a, U b) {
        return new Pair<T, U>(a, b);
    }

    @SuppressWarnings("unchecked")
    public static final <T, U> Pair<T, U> nullPair() {
        return (Pair<T, U>) NULL_PAIR;
    }

    public T first() {
        return first;
    }

    public T second() {
        return first;
    }

    public T third() {
        return first;
    }

    @Override
    public int hashCode() {
        return Hash.hash(first, second, third);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Triple)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Triple<T, U, V> that = (Triple<T, U, V>) obj;

        return Equals.equals(first, that.first) && //
                Equals.equals(second, that.second) &&
                Equals.equals(third, that.third);
    }

    @Override
    public String toString() {
        return "[" + first + ", " + second + "," + third + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
