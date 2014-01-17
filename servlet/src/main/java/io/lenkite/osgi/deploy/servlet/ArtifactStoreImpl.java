//package io.lenkite.osgi.deploy.servlet;
//
//import io.lenkite.osgi.types.base.ArtifactProfile;
//
//import java.util.Comparator;
//import java.util.LinkedHashSet;
//import java.util.Set;
//import java.util.concurrent.ConcurrentSkipListSet;
//
///**
// * @author: Tarun Elankath
// */
//public class ArtifactStoreImpl {
//    private static Comparator<ArtifactProfile> NAME_ONLY_COMPARATOR = new Comparator<ArtifactProfile>() {
//        public int compare(final ArtifactProfile o1, final ArtifactProfile o2) {
//            return o1.getSymbolicName().compareTo(o2.getSymbolicName());
//        }
//    };
//    private final Set<ArtifactProfile> artifacts = new ConcurrentSkipListSet<ArtifactProfile>(NAME_ONLY_COMPARATOR);
//
//    public Set<ArtifactProfile> getDeployedBundles() {
//        return new LinkedHashSet<ArtifactProfile>(bundles);
//    }
//
//    public void addDeployedBundle(final ArtifactProfile bundleProfile) {
//        bundles.remove(bundleProfile);
//        bundles.add(bundleProfile);
//    }
//
//
//}
