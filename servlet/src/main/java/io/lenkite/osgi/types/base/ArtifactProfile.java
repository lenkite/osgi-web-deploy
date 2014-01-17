package io.lenkite.osgi.types.base;

import java.io.Serializable;

/**
 * Artifact Descriptor. Immutable object.
 *
 * @author: Tarun Elankath
 */
public class ArtifactProfile implements Serializable {
    private final String symbolicName;
    private final Version version;
    private final ArtifactType artifactType;
    private final long modifyTime;
    private final long size;
    private final int id;
    private final MavenCoordinate coordinate;
    private final String fileName;

    public ArtifactProfile(String symbolicName, Version version, final ArtifactType artifactType, final String fileName, long modifyTime, long size) {
        this(symbolicName, version, artifactType, fileName, modifyTime, size, -1, MavenCoordinate.NIL);
    }

    public ArtifactProfile(String symbolicName, Version version, final ArtifactType artifactType, final String fileName, long modifyTime, long size, MavenCoordinate coordinate) {
        this(symbolicName, version, artifactType, fileName, modifyTime, size, -1, coordinate);
    }

    public ArtifactProfile(String symbolicName, Version version, final ArtifactType artifactType, final String fileName, long modifyTime, long size, int id, MavenCoordinate coordinate) {
        this.symbolicName = symbolicName;
        this.version = version == null ? Version.emptyVersion : version;
        this.artifactType = artifactType;
        this.fileName = fileName;
        this.modifyTime = modifyTime;
        this.size = size;
        this.id = id;
        this.coordinate = coordinate;
    }

    public ArtifactProfile withId(final int id) {
        return new ArtifactProfile(symbolicName, version, artifactType, fileName, modifyTime, size, id, coordinate);
    }

    public ArtifactProfile withModifyTime(final long modifyTime) {
        return new ArtifactProfile(symbolicName, version, artifactType, fileName, modifyTime, size, id, coordinate);
    }

    public String getSymbolicName() {
        return symbolicName;
    }

    public Version getVersion() {
        return version;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public long getSize() {
        return size;
    }

    public int getId() {
        return id;
    }

}
