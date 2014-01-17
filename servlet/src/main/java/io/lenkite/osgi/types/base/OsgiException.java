package io.lenkite.osgi.types.base;

/**
 * Base Exception for OSGi for all code in {@code io.lenkite.osgi}.
 *
 * @author  Tarun Elankath
 */
public class OsgiException extends RuntimeException {
    private final ErrorCode code;

    public OsgiException(ErrorCode code, Throwable cause) {
        super(code.toString(), cause);
        this.code = code;
    }

    public OsgiException(ErrorCode code) {
        super(code.toString());
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}
