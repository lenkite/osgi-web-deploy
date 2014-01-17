package io.lenkite.osgi.intfs;

import io.lenkite.osgi.types.base.ErrorCode;
import io.lenkite.osgi.types.base.MavenCoordinate;
import io.lenkite.osgi.types.base.ArtifactProfile;

import java.io.File;

/**
 * Analyzes an artifact.
 * @author: I034796
 * @created: 1/17/14
 */
public interface ArtifactAnalyzer {

    ArtifactProfile analyze(final File file);

}
