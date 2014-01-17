package io.lenkite.osgi.types.base;

import java.util.EnumSet;
import java.util.Set;

/**
 * Represents type of a build-time artifact.
 *
 *
 * @author Tarun Ramakrishna Elankath
 * @version $Id$
 * @created 11/30/13
 */
public enum ArtifactType {
    BUNDLE, SUBSYSTEM, ASSEMBLY, PLAINJAR;
//    private static final Set<ArtifactType> compositeArtifacts = EnumSet.of(SUBSYSTEM, ASSEMBLY);
//
//    public boolean isComposite() {
//        return compositeArtifacts.contains(this);
//    }

}
