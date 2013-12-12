package io.lenkite.osgi.deploy.servlet;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * @author Tarun Elankath
 */
public class Unzipper {

    private static final int BUFFER = 2048;
    private final File extractDir;
    private final List<File> extractedFiles = new ArrayList<File>();
    private final URL source;
    private Logger log = Logger.getLogger(Unzipper.class.getName());

    public Unzipper(URL source, File extractDir) {
        this.source = source;
        this.extractDir = extractDir;
    }

    public static boolean closeQuietly(Closeable... closeables) {
        boolean allClosed = true;
        for (Closeable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (IOException e) {
                    allClosed = false;
                }
            }
        }
        return allClosed;
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        BufferedInputStream bin = new BufferedInputStream(in, BUFFER);
        BufferedOutputStream bos = new BufferedOutputStream(out, BUFFER);
        int count;
        byte data[] = new byte[BUFFER];
        try {
            while ((count = bin.read(data, 0, BUFFER)) != -1) {
                bos.write(data, 0, count);
            }
        } finally {
            bos.flush();
        }
    }

    private void createDir(final File dir) throws IOException {
        log.info("Creating directory: " + dir +  " if it doesn't exist");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Unable to create entry dir: " + dir);
        }
    }

    private boolean createParentDirs(final File outf) {
        final File parentFile = outf.getParentFile();
        return parentFile != null && !parentFile.exists() && parentFile.mkdirs();
    }

    public List<File> getExtractedFiles() {
        return extractedFiles;
    }

    public void unzip() throws IOException {
        createDir(extractDir);

        InputStream sourceIn = source.openStream();
        if (sourceIn == null) {
            throw new IOException("Cannot read from: " + source);
        }

        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(sourceIn));

        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            File outf = new File(extractDir, entry.getName());

            if (entry.isDirectory()) {
                createDir(outf);
            } else {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Extracting: " + entry + " into " + extractDir);
                }
                createParentDirs(outf); // sometimes directory entries are not stored

                FileOutputStream fout = null;
                try {
                    fout = new FileOutputStream(outf);
                    copy(zis, fout);
                    extractedFiles.add(outf);
                } finally {
                    closeQuietly(fout);
                }
            }
        }

        zis.close();
        log.info("Successfully extracted zip source " + source + " into " + extractDir);
    }

}
