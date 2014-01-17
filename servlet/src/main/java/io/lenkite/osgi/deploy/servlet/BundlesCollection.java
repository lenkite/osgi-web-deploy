//package io.lenkite.osgi.deploy.servlet;
//
//import io.lenkite.osgi.types.base.BundleProfile;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.Serializable;
//import java.util.*;
//
///**
// * @author Tarun Ramakrishna Elankath
// * @version $Id$
// * @created 11/14/13
// */
//public class BundlesCollection implements Serializable {
//    private final Set<BundleProfile> bundles = new LinkedHashSet<BundleProfile>();
//
//
//    public BundlesCollection() {
//
//    }
//
//    public String toJson() {
//        JSONObject res = new JSONObject();
//        JSONArray bundlesArray = new JSONArray();
//        for (BundleProfile b: bundles) {
//            bundlesArray.put(new JSONObject(b));
//        }
//        res.put("bundles", bundlesArray);
//        return res.toString(2);
//    }
//
//
//    public Set<BundleProfile> getBundles() {
//        return bundles;
//    }
//}
