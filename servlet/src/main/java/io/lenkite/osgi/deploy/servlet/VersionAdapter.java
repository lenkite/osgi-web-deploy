package io.lenkite.osgi.deploy.servlet;

import io.lenkite.osgi.types.base.Version;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * A JAXB Adapter for {@link io.lenkite.osgi.types.base.Version} that marshals/unmarshals to/from a string value.
 * @author: I034796
 */
public class VersionAdapter extends XmlAdapter<String, Version> {
    @Override
    public Version unmarshal(final String v) throws Exception {
        if (v == null || v.isEmpty()) {
            return Version.emptyVersion;
        } else {
            return Version.parseVersion(v);
        }
    }

    @Override
    public String marshal(final Version v) throws Exception {
        if (v != null) {
            return v.toString();
        } else {
            return Version.emptyVersion.toString();
        }
    }
}
