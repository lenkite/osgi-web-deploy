package io.lenkite.osgi.deploy.servlet;

import org.json.JSONObject;

import java.io.Serializable;
import java.net.URL;
import java.util.UUID;

/**
 * Bundle Resource Descriptor POJO. Immutable object.
 *
 * @author: Tarun Elankath
 */
public class BundleResource implements Serializable {
    private final String parentUrl;
    private final String symbolicName;
    private final String version;
    private final long lastModified;
    private final long deployedOn;
    private final long size;
    private final int id;


    public BundleResource(String parentUrl, String symbolicName, String version, long lastModified, long deployedOn, long size) {
        this(parentUrl, symbolicName, version, lastModified, deployedOn, size, -1);
    }

    public BundleResource(String parentUrl, String symbolicName, String version, long lastModified, long deployedOn, long size, int id) {
        this.parentUrl = parentUrl;
        this.symbolicName = symbolicName;
        this.version = version;
        this.lastModified = lastModified;
        this.deployedOn = deployedOn;
        this.size = size;
        this.id = id;
    }

    public BundleResource withId(final int id) {
        return new BundleResource(parentUrl, symbolicName, version, lastModified, deployedOn, size, id);
    }

    public BundleResource withUpdate(final String version, final long lastModified, final long deployedOn, final long size) {
        return new BundleResource(parentUrl, symbolicName, version, lastModified, deployedOn, size, id);
    }

    public String getHref() {
        final String resourcePath = (symbolicName + "/" + version).replace('.', '-');
        return parentUrl + (parentUrl.endsWith("/") ? "" : "/") + resourcePath;
    }

    public String getSymbolicName() {
        return symbolicName;
    }

    public String getVersion() {
        return version;
    }

    public long getLastModified() {
        return lastModified;
    }

    public long getDeployedOn() {
        return deployedOn;
    }

    public long getSize() {
        return size;
    }

    public int getId() {
        return id;
    }

    public String toJson() {
        return new JSONObject(this).toString(2);
    }

    public String toString() {
        return new JSONObject(this).toString();
    }
}
