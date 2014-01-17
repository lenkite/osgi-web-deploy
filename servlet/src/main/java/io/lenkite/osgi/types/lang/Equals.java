package io.lenkite.osgi.types.lang;

/**
 * @author: Tarun R Elankath
 */
public final class Equals {
    private Equals() {
    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static boolean equals(boolean a, boolean b) {
        return a == b;
    }

    public static boolean equals(byte a, byte b) {
        return a == b;
    }

    public static boolean equals(short a, short b) {
        return a == b;
    }

    public static boolean equals(int a, int b) {
        return a == b;
    }

    public static boolean equals(long a, long b) {
        return a == b;
    }

    public static boolean equals(char a, char b) {
        return a == b;
    }

    public static boolean equals(float a, float b) {
        return a == b;
    }

    public static boolean equals(double a, double b) {
        return a == b;
    }

}
