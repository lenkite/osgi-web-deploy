package io.lenkite.osgi.deploy.impl;

import io.lenkite.osgi.intfs.ArtifactAnalyzer;
import io.lenkite.osgi.types.base.*;
import org.w3c.dom.Document;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static io.lenkite.osgi.types.base.BasicCode.ARTIFACT_INVALID;
import static io.lenkite.osgi.types.base.BasicCode.ARTIFACT_UNSUPPORTED;
import static java.util.logging.Level.SEVERE;

/**
 * @author Tarun Ramakrishna Elankath
 * @version $Id$
 * @created 11/30/13
 */
public class ArtifactAnalyzerImpl implements ArtifactAnalyzer {
    private static final Logger log = Logger.getLogger(ArtifactAnalyzer.class.getName());
    private static boolean xpathsInitalized = false;
    private static XPathExpression POM_PROJ_GID;
    private static XPathExpression POM_PROJ_AID;
    private static XPathExpression POM_PROJ_VER;
    private static XPathExpression POM_PARENT_VER;
    private static XPathExpression POM_PARENT_GID;
    //    private final Artifact artifact;
//    private String name;
//    private long modified;
//    private Version version;
//    private ArtifactType artifactType;


//    public ArtifactAnalyzerImpl(final MavenCoordinate coordinate, final File artifactFile) {
//        this(coordinate, artifactFile, null);
//    }
//
//    public ArtifactAnalyzerImpl(final MavenCoordinate coordinate, final File artifactFile, final ArtifactProfile containedIn) {
//        this.coordinate = coordinate;
//        this.artifactFile = artifactFile;
//        this.containedIn = containedIn;
//    }
//
//    public ArtifactAnalyzerImpl(final File artifactFile, final ArtifactProfile containedIn) {
//        this.artifactFile = artifactFile;
//        this.containedIn = containedIn;
////        this.coordinate = loadCoordinate(artifactFile);
//        this.coordinate = MavenCoordinate.NIL;
//    }

    private static void loadXPathExpressions() {
        if (!xpathsInitalized) {
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            xpath.setNamespaceContext(new NamespaceContext() {
                @Override
                public String getNamespaceURI(final String prefix) {
                    return prefix.equals("m") ? "http://maven.apache.org/POM/4.0.0" : null;
                }

                @Override
                public String getPrefix(final String namespaceURI) {
                    return namespaceURI.equals("http://maven.apache.org/POM/4.0.0") ? "m" : null;
                }

                @Override
                public Iterator getPrefixes(final String namespaceURI) {
                    final List<String> prefixes = new ArrayList<String>();
                    if (namespaceURI.equals("http://maven.apache.org/POM/4.0.0")) {
                        prefixes.add("m");
                    }
                    return prefixes.iterator();
                }
            });
//            XPathContext
            final String projGid = "/m:project/m:groupId/text()";
            final String projArtifactId = "/m:project/m:artifactId";
            final String projVersion = "/m:project/m:version";
            final String parentGid = "/m:project/m:parent/m:groupId";
            final String parentVersion = "/m:project/m:parent/m:version";
            try {
                POM_PROJ_GID = xpath.compile(projGid);
                POM_PROJ_AID = xpath.compile(projArtifactId);
                POM_PROJ_VER = xpath.compile(projVersion);
                POM_PARENT_GID = xpath.compile(parentGid);
                POM_PARENT_VER = xpath.compile(parentVersion);
                xpathsInitalized = true;
            } catch (XPathExpressionException e) {
                log.log(SEVERE, "Error compiling xpath", e);
                throw new RuntimeException(e);
            }
        }

    }

    static Document loadPom(ZipFile zf) throws Exception {
        final Enumeration<? extends ZipEntry> entries = zf.entries();
        Document doc = null;
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        final DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        while (entries.hasMoreElements()) {
            final ZipEntry entry = entries.nextElement();
            final String entryName = entry.getName();
            if (entryName.startsWith("META-INF/maven") && entryName.endsWith("/pom.xml")) {
                final InputStream inputStream = zf.getInputStream(entry);
                doc = documentBuilder.parse(inputStream);
                break;
            }
        }
        return doc;
    }

    static Attributes getMainAttributes(final ZipFile zf, final String entryName) throws IOException {
        final ZipEntry entry = zf.getEntry(entryName);
        if (entry != null) {
            final InputStream inputStream = zf.getInputStream(entry);
            final Manifest manifest = new Manifest(inputStream);
            final Attributes attributes = manifest.getMainAttributes();
            return attributes;
        }
        return null;
    }

    public static MavenCoordinate loadCoordinate(final ZipFile artifactFile) {
        MavenCoordinate coord = MavenCoordinate.NIL;
        final ZipFile zf = artifactFile;
        try {
            final Document pomDoc = loadPom(zf);
            loadXPathExpressions();
            final String projAid = POM_PROJ_AID.evaluate(pomDoc);
            final String parentGid = POM_PARENT_GID.evaluate(pomDoc);
            final String parentVer = POM_PARENT_VER.evaluate(pomDoc);

            String projGid = POM_PROJ_GID.evaluate(pomDoc);
            String projVer = POM_PROJ_VER.evaluate(pomDoc);
            if (projGid == null || projGid.isEmpty()) {
                projGid = parentGid;
            }
            if (projVer == null || projVer.isEmpty()) {
                projVer = parentVer;
            }
            if (projAid != null && !projAid.isEmpty() && projGid != null && !projGid.isEmpty()) {
                coord = new MavenCoordinate(projGid, projAid, projVer);
            }
        } catch (final Exception e) { //NOPMD //NOSONAR
            log.log(Level.WARNING, "Unable to calculate coordinate for: " + artifactFile + " Returning: " + coord);
        }
        return coord;
    }

    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }

    public static void closeQuietly(ZipFile c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public ArtifactProfile analyze(final File artifactFile) {
        final File file = artifactFile;
        final Result r = doAnalyze(artifactFile);
        final ArtifactProfile profile = new ArtifactProfile(r.name, r.version, r.artifactType, file.getName(), r.modified, artifactFile.length(), r.coordinate);
        return profile;
    }

    private Version convert(String version) {
        if (version == null || version.isEmpty()) {
            return Version.emptyVersion;
        }
        //TODO: fix this hacky shit.
        final char[] versionChars = new char[version.length()];
        int dotCount = 0;
        int j = 0;
        for (char c : version.toCharArray()) {
            switch (c) {
                case '-':
                    if (dotCount < 3) {
                        versionChars[j++] = '.';
                        continue;
                    }
                case '.':
                    dotCount++;
                default:
                    versionChars[j++] = c;
            }
        }
        return Version.parseVersion(new String(versionChars));
    }

    /**
     * Attempts to parse artifact file as zip file sub-type and set type, name, version, modified
     *
     * @return
     * @throws java.io.IOException
     */
    private Result doAnalyze(final File artifactFile) {
        final Result r = new Result();
        final File f = artifactFile;
        ZipFile zf = null;
        try {
            zf = new ZipFile(f);
            r.coordinate = loadCoordinate(zf);
            Attributes attrs = getMainAttributes(zf, "OSGI-INF/SUBSYSTEM.MF");
            if (attrs != null) {
                r.name = attrs.getValue("Subsystem-SymbolicName");
                String versionStr = attrs.getValue("Subsystem-Version");

                r.version = convert(versionStr);
                r.artifactType = ArtifactType.SUBSYSTEM;
                r.modified = zf.getEntry("OSGI-INF").getTime();
                return r;
            }
            attrs = getMainAttributes(zf, "META-INF/MANIFEST.MF");
            if (attrs != null) {
                String symbolicName = attrs.getValue("Bundle-SymbolicName");
                if (symbolicName != null) {
                    //Bundle symbolic name can contain additional attributes like singleton=true
                    int separatorIndex = symbolicName.indexOf(";");
                    if (separatorIndex > 0) {
                        symbolicName = symbolicName.substring(0, separatorIndex).trim();
                    }
                }

                String versionStr = attrs.getValue("Subsystem-Version");
                r.version = convert(versionStr);
                if (symbolicName == null) {
                    r.artifactType = ArtifactType.PLAINJAR;
                    r.name = f.getName();
                } else {
                    r.name = symbolicName;
                    r.artifactType = ArtifactType.BUNDLE;
                    String lastModStr = attrs.getValue("Bnd-LastModified");
                    if (lastModStr != null) {
                        try {
                            r.modified = Long.parseLong(lastModStr);
                        } catch (NumberFormatException e) {
                            log.warning("Cannot parse as long Bnd-LastModified: " + lastModStr);
                        }
                    }
                }
                if (r.modified == 0) {
                    r.modified = zf.getEntry("META-INF").getTime();
                }
            } else {
                log.severe("Invalid artifact: " + artifactFile);
                throw new OsgiException(ARTIFACT_INVALID);
            }
            return r;
        } catch (IOException e) {
            throw new OsgiException(ARTIFACT_UNSUPPORTED, e);
        } finally {
            if (zf != null) {
                closeQuietly(zf);
            }
        }
    }

    private class Result {
        private String name;
        private Version version = Version.emptyVersion;
        private long modified = 0;
        private ArtifactType artifactType;
        private MavenCoordinate coordinate;
    }
//    private void analyze(final BufferedInputStream fstream) throws IOException {
//        try {
//            fstream.mark(2 * 1024);
//            JarInputStream jarin = new JarInputStream(fstream);
//            final Manifest manifest = jarin.getManifest();
//            final Attributes mainAttrs = manifest.getMainAttributes();
//            artifactType = ArtifactType.PLAINJAR;
//            String symbolicName = mainAttrs.getValue("Bundle-SymbolicName");
//            int separatorIdx = symbolicName.indexOf(';');
//            if (separatorIdx != -1) {
//                //aah we have stupid attributes as part of symbolic name
//                symbolicName = symbolicName.substring(0, separatorIdx).trim();
//            }
//            if (symbolicName == null || symbolicName.isEmpty()) {
//                return;
//            }
//            String version = mainAttrs.getValue("Bundle-Version");
//            if (version == null) {
//                throw new IllegalArgumentException("Bundle: " + artifact.getFile() + " does not have Bundle-Version header");
//            }
//            this.name = symbolicName;
//            String lastModStr = mainAttrs.getValue("Bnd-LastModified");
//            if (lastModStr != null) {
//                this.modified = Long.parseLong(lastModStr);
//            } else {
//                fstream.reset();
//                fstream.mark(2 * 1024);
//                final ZipInputStream zin = new ZipInputStream(fstream);
//                this.modified = zin.getNextEntry().getTime(); //assumes META-INF as first entry
//            }
////            else if (version.matches(".*\\.\\d{12}$")) { //Ex:
////                //TODO: parse last component of bundle version date as last-modified.
////                //AARGH due to maven bug where tstamp is local time zone this is useless
////                lastModStr = version.substring(version.lastIndexOf('.') + 1, version.length());
////            }
////            this.modified = artifact.getFile().lastModified();
//        } catch (IOException e) {
//            fstream.reset();
//            return false;
//        }
//        return false;
//    }
}
