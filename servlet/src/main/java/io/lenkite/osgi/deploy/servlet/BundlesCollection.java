package io.lenkite.osgi.deploy.servlet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author Tarun Ramakrishna Elankath
 * @version $Id$
 * @created 11/14/13
 */
public class BundlesCollection implements Serializable {
//    private Queue<BundleResource> bundles = new ConcurrentLinkedQueue<BundleResource>();
    private final Set<BundleResource> bundles = new ConcurrentSkipListSet<BundleResource>(NAME_ONLY_COMPARATOR);

    private static Comparator<BundleResource> NAME_ONLY_COMPARATOR = new Comparator<BundleResource>() {
        public int compare(BundleResource o1, BundleResource o2) {
           return o1.getSymbolicName().compareTo(o2.getSymbolicName());
        }
    };

    public BundlesCollection() {

    }

    public String toJson() {
        JSONObject res = new JSONObject();
        JSONArray bundlesArray = new JSONArray();
        for (BundleResource b: bundles) {
            bundlesArray.put(new JSONObject(b));
        }
        res.put("bundles", bundlesArray);
        return res.toString(2);
    }
}
