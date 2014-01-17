package io.lenkite.osgi.types.base;

import io.lenkite.osgi.deploy.servlet.VersionAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Represents an OSGi version.
 *
 * @author I034796
 */
@XmlJavaTypeAdapter(VersionAdapter.class)
@XmlAccessorType(XmlAccessType.FIELD)
public class Version implements Serializable {
    /**
     * The empty version "0.0.0".
     */
    public static final Version emptyVersion = new Version(0, 0, 0);
    private static final String SEPARATOR = ".";
    private final int major;
    private final int minor;
    private final int micro;
    private final String qualifier;
    private transient String versionString;

    public Version() {
        major = 0;
        minor = 0;
        micro = 0;
        qualifier = "";
    }

    /**
     * Created a version identifier from the specified string.
     * <p/>
     * <p/>
     * Here is the grammar for version strings.
     * <p/>
     * <pre>
     * version ::= major('.'minor('.'micro('.'qualifier)?)?)?
     * major ::= digit+
     * minor ::= digit+
     * micro ::= digit+
     * qualifier ::= (alpha|digit|'_'|'-')+
     * digit ::= [0..9]
     * alpha ::= [a..zA..Z]
     * </pre>
     * <p/>
     * There must be no whitespace in version.
     *
     * @param version String representation of the version identifier.
     * @throws IllegalArgumentException If {@code version} is improperly
     *                                  formatted.
     */
    public Version(String version) {
        int maj = 0;
        int min = 0;
        int mic = 0;
        String qual = "";

        try {
            StringTokenizer st = new StringTokenizer(version, SEPARATOR, true);
            maj = Integer.parseInt(st.nextToken());

            if (st.hasMoreTokens()) { // minor
                st.nextToken(); // consume delimiter
                min = Integer.parseInt(st.nextToken());

                if (st.hasMoreTokens()) { // micro
                    st.nextToken(); // consume delimiter
                    mic = Integer.parseInt(st.nextToken());

                    if (st.hasMoreTokens()) { // qualifier
                        st.nextToken(); // consume delimiter
                        qual = st.nextToken(""); // remaining string

                        if (st.hasMoreTokens()) { // fail safe
                            throw new IllegalArgumentException(
                                    "invalid format: " + version);
                        }
                    }
                }
            }
        } catch (NoSuchElementException e) {
            IllegalArgumentException iae = new IllegalArgumentException(
                    "invalid format: " + version);
            iae.initCause(e);
            throw iae;
        }

        major = maj;
        minor = min;
        micro = mic;
        qualifier = qual;
        validate();
    }

    public Version(int major, int minor, int micro) {
        this(major, minor, micro, null);
    }

    public Version(int major, int minor, int micro, String qualifier) {
        this.major = major;
        this.minor = minor;
        this.micro = micro;
        if (qualifier == null) {
            this.qualifier = "";
        } else {
            this.qualifier = qualifier;
        }
        validate();
    }

    /**
     * Parses a version identifier from the specified string.
     * <p/>
     * <p/>
     * See {@code Version(String)} for the format of the version string.
     *
     * @param version String representation of the version identifier. Leading
     *                and trailing whitespace will be ignored.
     * @return A {@code Version} object representing the version
     *         identifier. If {@code version} is {@code null} or
     *         the empty string then {@code emptyVersion} will be
     *         returned.
     * @throws IllegalArgumentException If {@code version} is improperly
     *                                  formatted.
     */
    public static Version parseVersion(String version) {
        if (version == null) {
            return emptyVersion;
        }

        version = version.trim();
        if (version.length() == 0) {
            return emptyVersion;
        }

        return new Version(version);
    }

    /**
     * Called by the Version constructors to validate the version components.
     *
     * @throws IllegalArgumentException If the numerical components are negative
     *                                  or the qualifier string is invalid.
     */
    private void validate() {
        if (major < 0) {
            throw new IllegalArgumentException("negative major");
        }
        if (minor < 0) {
            throw new IllegalArgumentException("negative minor");
        }
        if (micro < 0) {
            throw new IllegalArgumentException("negative micro");
        }
        char[] chars = qualifier.toCharArray();
        for (int i = 0, length = chars.length; i < length; i++) {
            char ch = chars[i];
            if (('A' <= ch) && (ch <= 'Z')) {
                continue;
            }
            if (('a' <= ch) && (ch <= 'z')) {
                continue;
            }
            if (('0' <= ch) && (ch <= '9')) {
                continue;
            }
            if ((ch == '_') || (ch == '-')) {
                continue;
            }
            throw new IllegalArgumentException("invalid qualifier: "
                    + qualifier);
        }
    }

    /**
     * Compares this {@code Version} object to another object.
     * <p/>
     * <p/>
     * A version is considered to be <b>equal to </b> another version if the
     * major, minor and micro components are equal and the qualifier component
     * is equal (using {@code String.equals}).
     *
     * @param object The {@code Version} object to be compared.
     * @return {@code true} if {@code object} is a
     *         {@code Version} and is equal to this object;
     *         {@code false} otherwise.
     */
    public boolean equals(Object object) {
        if (object == this) { // quicktest
            return true;
        }

        if (!(object instanceof Version)) {
            return false;
        }

        Version other = (Version) object;
        return (major == other.major) && (minor == other.minor)
                && (micro == other.micro) && qualifier.equals(other.qualifier);
    }

    @Override
    public int hashCode() {
        return (major << 24) + (minor << 16) + (micro << 8)
                + qualifier.hashCode();
    }

    /**
     * Returns the major component of this version identifier.
     *
     * @return The major component.
     */
    public int getMajor() {
        return major;
    }

    /**
     * Returns the minor component of this version identifier.
     *
     * @return The minor component.
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Returns the micro component of this version identifier.
     *
     * @return The micro component.
     */
    public int getMicro() {
        return micro;
    }

    /**
     * Returns the qualifier component of this version identifier.
     *
     * @return The qualifier component.
     */
    public String getQualifier() {
        return qualifier;
    }

    /**
     * Returns the string representation of this version identifier.
     * <p/>
     * <p/>
     * The format of the version string will be {@code major.minor.micro}
     * if qualifier is the empty string or
     * {@code major.minor.micro.qualifier} otherwise.
     *
     * @return The string representation of this version identifier.
     */
    public String toString() {
        if (versionString != null) {
            return versionString;
        }
        int q = qualifier.length();
        StringBuffer result = new StringBuffer(20 + q);
        result.append(major);
        result.append(SEPARATOR);
        result.append(minor);
        result.append(SEPARATOR);
        result.append(micro);
        if (q > 0) {
            result.append(SEPARATOR);
            result.append(qualifier);
        }
        return versionString = result.toString();
    }
}
