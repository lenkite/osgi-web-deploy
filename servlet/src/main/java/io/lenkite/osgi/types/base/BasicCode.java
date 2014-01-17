package io.lenkite.osgi.types.base;

/**
 * Common error codes.
 *
 * @author: I034796
 */
public enum BasicCode implements ErrorCode {

    ARTIFACT_INVALID(1000),
    ARTIFACT_UNSUPPORTED(1001);

    private final int number;

    private BasicCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }
}
