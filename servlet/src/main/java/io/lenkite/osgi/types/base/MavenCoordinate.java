package io.lenkite.osgi.types.base;

import java.io.Serializable;

/**
 * @author Tarun Ramakrishna Elankath
 */
public class MavenCoordinate implements Serializable {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String type;
    private final String classifier;

    /**
     * Used to represent an unknown or uncalculated maven coordinate.
     */
    public static final MavenCoordinate NIL = new MavenCoordinate("nil", "nil", "0.0", ".nil");

    public MavenCoordinate(String groupId, String artifactId, String version, String type, String classifier) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.type = type == null || type.isEmpty() ? "jar" : type ;
        this.classifier = classifier == null ? "": classifier;
    }

    public MavenCoordinate(String groupId, String artifactId, String version, String type) {
        this(groupId, artifactId, version, type, null);
    }

    public MavenCoordinate(String groupId, String artifactId, String version) {
        this(groupId, artifactId, version,  null);
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getType() {
        return type;
    }

    public String getClassifier() {
        return classifier;
    }


    /**
     The artifact coordinates in the format
     *            {@code <groupId>:<artifactId>[:<type>[:<classifier>]]:<version>}
     */
    @Override
    public String toString() {
            StringBuilder buffer = new StringBuilder( 128 );
            buffer.append( getGroupId() );
            buffer.append( ':' ).append( getArtifactId() );
            buffer.append( ':' ).append( getType() );
            if ( getClassifier().length() > 0 )
            {
                buffer.append( ':' ).append( getClassifier() );
            }
            buffer.append( ':' ).append( getVersion() );
            return buffer.toString();
    }
}
