package io.lenkite.osgi.deploy.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * A <em>REST-compliant</em> servlet supporting {@code form-upload} of bundles and can be consumed in a web-application via declarations in the consumer's {@code web.xml}.
 * <p>
 * This deploy servlet does not declare annotations. It is required that the
 * servlet be declared in {@code web.xml} of the consuming web application with
 * a {@code multipart-config} element declaration.
 * </p>
 * <pre>
 * {@code
 * <servlet>
 *     <display-name>DeployServlet</display-name>
 *     <servlet-name>DeployServlet</servlet-name>
 *     <servlet-class>io.lenkite.osgi.deploy.servlet.DeployServlet</servlet-class>
 *     <multipart-config>
 *     </multipart-config>
 *      <!-- WARNING: many servlet containers have a bug that require explicit close tag for 'multipart-config'-->
 * </servlet>
 * }
 * </pre>
 * <p/>
 * Please see documentation of REST API in README.md. (javadocs suck as a medium for any serious long-length documentation)
 *
 * @author Tarun Ramakrishna Elankath
 * @version $Id$
 */
@SuppressWarnings("serial")
public class DeployServlet extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        final PrintWriter out = resp.getWriter();
        try {
            out.println("Received Request on host: " + req.getLocalName() + ", socket address:" + req.getLocalAddr() + ":"
                    + req.getLocalPort() + " at time: " + new Date(System.currentTimeMillis()) + " from client: " + req.getRemoteAddr());
        } catch (Throwable t) {
            t.printStackTrace(out);
        } finally {
            out.flush();
        }
    }

    //    public static final String SAVE_DIR_PARAM = "savedir";
//    private static final int BUFFER = 1024 * 4;
//    private static Logger log = Logger.getLogger(DeployServlet.class.getName());
//    private File saveDir; //TODO: make this configurable.
//
//    public static void copy(InputStream in, OutputStream out) throws IOException {
//        BufferedInputStream bin = new BufferedInputStream(in, BUFFER);
//        BufferedOutputStream bos = new BufferedOutputStream(out, BUFFER);
//        int count;
//        byte data[] = new byte[BUFFER];
//        try {
//            while ((count = bin.read(data, 0, BUFFER)) != -1) {
//                bos.write(data, 0, count);
//            }
//        } finally {
//            bos.flush();
//        }
//    }
//
//    public static boolean closeQuietly(Closeable... closeables) {
//        boolean allClosed = true;
//        for (Closeable c : closeables) {
//            if (c != null) {
//                try {
//                    c.close();
//                } catch (IOException e) {
//                    allClosed = false;
//                }
//            }
//        }
//        return allClosed;
//    }
//
//    @Override
//    public void init(ServletConfig config) throws ServletException {
//        super.init(config);
//        final String paramVal = config.getInitParameter(SAVE_DIR_PARAM);
//        if (paramVal != null && paramVal.trim().length() > 0) {
//            this.saveDir = new File(paramVal);
//        } else {
//            log.warning("'savedir' parameter missing or empty. Will use default.");
//        }
//        boolean useDefaultdir = saveDir == null;
//        if (!useDefaultdir) {
//            if (!saveDir.exists()) {
//                if (!saveDir.mkdirs()) {
//                    log.warning("Unable to create save directory: " + saveDir + " specified by servlet context attribute: "
//                            + SAVE_DIR_PARAM + ". Will default to <javax.servlet.context.tempdir>/upload'");
//                    useDefaultdir = true;
//                }
//            }
//        }
//
//        if (useDefaultdir) {
//            File tempDir = (File) config.getServletContext().getAttribute("javax.servlet.context.tempdir");
//            if (tempDir == null) {
//                log.warning("Attribute javax.servlet.context.tempdir is NULL!. Defaulting to 'upload' as save directory");
//                saveDir = new File("upload");
//            } else {
//                saveDir = new File(tempDir, "upload");
//                if (!saveDir.mkdirs()) {
//                    throw new ServletException("Unable to create default save directory: " + saveDir);
//                }
//            }
//        }
//        log.info("Using directory: " + saveDir + " as directory for saving uploads");
//    }
//
//    @Override
//    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//        super.doOptions(req, resp);
//    }
//
//    @Override
//    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//    }
//
//    @Override
//    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//        final String extraPath = req.getPathInfo();
//        final URL servletUrl = getServletUrl(req);
//        if (extraPath == null || extraPath.isEmpty() || extraPath.trim().equals("/")) {
//            final Set<UploadResource> resources = new HashSet<UploadResource>();
//            resp.setContentType("application/json");
//            if (saveDir.exists()) {
//                for (File f : saveDir.listFiles()) {
//                    resources.add(new UploadResource(servletUrl, f));
//                }
//            }
//            final UploadCollection uploadCollection = new UploadCollection(resources);
//            uploadCollection.writeJson(resp.getWriter());
//        } else {
//            final File resource = new File(saveDir, extraPath);
//            resp.setContentType("application/force-download");
//            resp.setContentLength((int) resource.length());
//            InputStream source = null;
//            OutputStream dest = null;
//            try {
//                source = new BufferedInputStream(new FileInputStream(resource));
//                final String fileName = resource.getName();
//                resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//                final String contentType = new MimetypesFileTypeMap().getContentType(fileName);
//                resp.setContentType(contentType);
//                dest = resp.getOutputStream();
//                copy(source, dest);
//            } catch (FileNotFoundException e) {
//                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Cannot retrieve file  identified by : " + extraPath
//                        + " from save directory.");
//            } finally {
//                closeQuietly(source, dest);
//            }
//        }
//    }
//
//    @Override
//    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
//        final String extraPath = req.getPathInfo();
//        resp.setContentType("text/plain");
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//        final PrintWriter out = resp.getWriter();
//        if (extraPath == null || extraPath.isEmpty() || extraPath.equals("/")) {
//            out.println("No resource to delete since extra path = " + extraPath);
//        } else {
//            final File targf = new File(saveDir, extraPath);
//            if (!targf.exists()) {
//                out.println("Target file: " + targf + " does not exist.");
//            } else {
//                out.println("Target file: " + targf + " deleted = " + targf.delete());
//            }
//        }
//    }
//
//    @Override
//    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//        Collection<Part> parts = req.getParts();
//        UploadCollection uploaded = new UploadCollection();
//        int partNum = 0;
//        for (Part part : parts) {
//            final String partLabel = "(" + part.getName() + ", " + partNum++ + ")";
//            final String fileName = getFileName(part);
//            if (fileName == null || fileName.isEmpty()) {
//                continue;
//            }
//            if (log.isLoggable(Level.FINE)) {
//                log.fine("Obtained file name: '" + fileName + "' for part: " + partLabel);
//            }
//            final InputStream source = part.getInputStream();
//            final File targFile = new File(saveDir, fileName);
//            final OutputStream dest = new FileOutputStream(targFile);
//            try {
//                copy(source, dest);
//            } finally {
//                closeQuietly(source, dest);
//            }
//            log.info("Successfully saved part: " + partLabel + " as file: " + targFile);
//            uploaded.addResource(getServletUrl(req), targFile);
//        }
//        uploaded.writeJson(resp.getWriter());
//    }
//
//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//
//        final String extraPath = req.getPathInfo();
//        final URL servletUrl = getServletUrl(req);
//        if (extraPath == null || extraPath.isEmpty() || extraPath.trim().equals("/")) {
//            //PUT must specificy relative path of resource explictly.
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "PUT must specify upload path of resource relative to save directory: ");
//        } else {
//            final boolean extract = req.getParameter("extract") != null;
//            //            final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
//            final File targFile = new File(saveDir, extraPath);
//            final File parentDir = targFile.getParentFile();
//            if (!parentDir.exists() && !parentDir.mkdirs()) {
//                throw new IOException("Unable to create directory: " + parentDir);
//            }
//            final InputStream srcIn = new BufferedInputStream(req.getInputStream(), BUFFER);
//            final OutputStream destOut = new BufferedOutputStream(new FileOutputStream(targFile), BUFFER);
//            try {
//                copy(srcIn, destOut);
//            } finally {
//                closeQuietly(srcIn, destOut);
//            }
//            log.info("Successfully wrote file to:" + targFile);
//            UploadCollection uploaded = new UploadCollection();
//            uploaded.addResource(servletUrl, targFile);
//            if (extract && targFile.exists() && targFile.getName().endsWith(".zip")) {
//                Unzipper unzipper = new Unzipper(targFile.toURI().toURL(), parentDir);
//                unzipper.unzip();
//                log.info("Successfully extracted: " + targFile + " into: " + parentDir);
//                final List<File> extractedFiles = unzipper.getExtractedFiles();
//                for (File ef : extractedFiles) {
//                    uploaded.addResource(servletUrl, ef);
//                }
//            }
//            uploaded.writeJson(resp.getWriter());
//        }
//    }
//
//    private String getFileName(Part part) {
//        final String cdispValue = part.getHeader("content-disposition");
//        if (log.isLoggable(Level.FINE)) {
//            log.fine("Content disposition header for part: " + part.getName() + " = " + cdispValue);
//        }
//        String fname = null;
//        if (cdispValue != null) {
//            for (String s : cdispValue.split(";")) {
//                if (s.trim().startsWith("filename")) {
//                    if (s.trim().startsWith("filename")) {
//                        fname = s.substring(s.indexOf("=") + 2, s.length() - 1);
//                        break;
//                    }
//                }
//            }
//        }
//        return fname;
//    }
//
//    private URL getServletUrl(HttpServletRequest req) throws MalformedURLException {
//        final String fullPath = req.getContextPath() + req.getServletPath();
//        final URL fullServletUrl = new URL(req.getScheme(), req.getServerName(), req.getServerPort(), fullPath);
//        log.fine("Full Servlet Url = " + fullServletUrl);
//        return fullServletUrl;
//    }
//
//    //    private Gson newGson() {
//    //        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
    //    }

}
//avax.servlet.context.tempdir