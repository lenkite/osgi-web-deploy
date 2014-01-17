package io.lenkite.osgi.types.base.trait;

import io.lenkite.osgi.types.base.Version;

/**
 * Indicates that the given entity posses a valid OSGi version. Consider moving this up into a new nm.types.base bundle.
 * @author: I034796
 */
public interface Versioned {
    Version getVersion();
}
