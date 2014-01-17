package io.lenkite.osgi.types.lang;

import java.util.Arrays;

public class Hash {
    private static final int PRIME = 31;

    private Hash() {
    }

    public static int hashCode(Object a) {
        return a == null ? 0 : a.hashCode();
    }

    public static int hash(Object a) {
        int result = 1;
        result = PRIME * result + ((a == null) ? 0 : a.hashCode());
        return result;
    }

    public static int hash(Object a, Object b) {
        int result = 1;
        result = PRIME * result + ((a == null) ? 0 : a.hashCode());
        result = PRIME * result + ((b == null) ? 0 : b.hashCode());
        return result;
    }

    public static int hash(Object a, Object b, Object c) {
        int result = 1;
        result = PRIME * result + ((a == null) ? 0 : a.hashCode());
        result = PRIME * result + ((b == null) ? 0 : b.hashCode());
        result = PRIME * result + ((c == null) ? 0 : c.hashCode());
        return result;
    }

    public static int hash(Object... instances) {
        return Arrays.hashCode(instances);
    }

    public static int extendHash(int hashCode, Object a) {
        int result = hashCode;

        result = PRIME * result + ((a == null) ? 0 : a.hashCode());
        return result;
    }

    public static int extendHash(int hashCode, Object a, Object b) {
        int result = hashCode;

        result = PRIME * result + ((a == null) ? 0 : a.hashCode());
        result = PRIME * result + ((b == null) ? 0 : b.hashCode());
        return result;
    }

    public static int extendHash(int hashCode, Object a, Object b, Object c) {
        int result = hashCode;

        result = PRIME * result + ((a == null) ? 0 : a.hashCode());
        result = PRIME * result + ((b == null) ? 0 : b.hashCode());
        result = PRIME * result + ((c == null) ? 0 : c.hashCode());
        return result;
    }

    public static int extendHash(int hashCode, Object... instances) {
        int result = hashCode;

        for (Object element : instances)
            result = PRIME * result + (element == null ? 0 : element.hashCode());
        return result;
    }

}

