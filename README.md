# osgi-web-upload

Tool that allows deployment of OSGi bundles in any OSGI compliant container via HTTP upload.

## Description
An OSGi compliant set of bundles with the following features:
- Deploy Servlet Bundle that leverages RFC 1867 Upload of artifacts using the Servlet 3.0 API and deploys bundle artifacts. This servlet can be the target
  of an `multipart-formdata` form. Currently only upload of bundle artifacts is possible. Will be extended to support OSGi subsystem artifacts in the future.
- Deploy Servlet provides JSON based REST API that exposes metadata about uploaded bundles.
  The metadata is simplistic at the moment:
	- 'name' 		 File name
	- 'md5' 		 MD5 hash of uploaded artifact
	- 'timestamp' 	 Timestamp 
	- 'size' 		 Artifact File size in bytes
	- 'url'			 Resource Url 
	- 'directory'	 If resource is a directory
	- 'symbolicName' Symbolic name if artifact is an OSGi bundle or OSGi subystem
	- 'coordinate'   Can be null. This is a composite object that consists of three fields: {groupId, artifactId, version}.
	- 'state'        If uploaded artifact is OSGI bundle, this is the OSGI bundle state in the runtime container.
	- 'deployTime'   Time when some client last deployed this artifact in UTC milliseconds.
- Separate Deploy WAB that provides a simple very simple form upload UI and exposes Deploy Servlet. Deploy Servlet is in a separate bundle from the WAB to allow
  users to leverage the deploy servlet independent of the web-app.  (WABS are generally highly environment specific).
- Deploy WAB Currently does not have any role set. You can add roles and login configs to the web.xml yourself if you choose to use this WAB.
- Deploy WAB context path is '/deploy'.
- Deploy client bundle that provides an API to scan local  directory and upload all changed artifacts to the upload WAB. Timestamps are preserved.
- Upload client shell script that leverages the upload client bundle and is invokable from the command line.

## Quick Start

* [Download the latest release](https://provideCentralMavenLink)
* Clone the repo: `git clone https://github.com/lenkite/osgi-http-upload` and build with Maven 3.0.4+.

## Deploy WAB UI

If you are using the default deploy WAB then it is accessible at `(host)/deploy`. This serves up a simple UI that allows one to deploy bundles and list deployed bundles.

## Deploy Servlet REST API
The Deploy servlet provides a REST API whose units are described below. `(context-path)` used below means expand to the full context path under which the servlet handles
all URLS. This is generally generally `web-context-name/servlet-path`. By default `servlet-path` is `service` if you are using the deploy WAB of this tool without changes.
In other words by default the the deploy servlet handles all URL's who have a path of of the form `deploy/service`.

### GET (host)/(context-path)/bundles/
 * Get the deployed bundles collection. This does *not* return all the bundles that are present in the OSGi runtime - only those that were deployed via the this deploy servlet.
 * By default serves a JSON response with `Content-Type: application/json` response unless the client explicitly requests `Accept: application/zip`.
 * The JSON response is a `BundleCollection`. A `BundleCollection` is a JSON object with a `bundles` array consisting of `BundleResource` objects.
 ```JSON
 {
  "bundles": [
    {
        "href": "<self href of bundle resource>",
        "id": "<integer bundle id in OSGi runtime>",
        "symbolicName": "<Bundle-SymbolicName>",
        "version": "<Bundle-Version>",
        "size": <SizeInBytes>",
        "lastModified": "<LastModifiedOfBundleInUTCMillis>", /*Checks Bnd-LastModified, falls back to 'deployedOn' */
        "deployedOn": "<DeploymentTimeInUtcMillis>",
    },
  ]
 }
 ```

### POST (host)/(context-path)/bundles/\[?nocheck\]
 * Expects the request to be of type `multipart/form-data`. TODO: what is content-type of part by chrome firefox ? needs testing.
 * Upload Parts must be valid OSGI bundles. We will refer to these valid OSGi bundles are `deploy-units`  or `deploy-unit` from now on.
 * If the runtime has no bundle with the same `Bundle-SymbolicName` (ignoring parameters), then the `deploy-unit` is freshly installed via `BundleContext.install`
 * If `nocheck` is un-specified and the runtime contains an already existing bundle with the same `Bundle-SymbolicName` (ignoring parameters), then a comparison if made on the `Bundle-Version`
    ** If the `Bundle-Version` matches, then the existing bundle is updated via `Bundle.update`.
    ** If it differs, then the existing bundle is un-installed via `BundleContext.uninstall` and the `deploy-unit` is installed. If there are dependents, then a _refresh_ is issued
       via `FrameworkWiring.refreshBundles` (passing in the un-installed bundles).
 * If `nocheck` is specified, then the behaviour is the same as a fresh deployment. Of course, if _both_ `Bundle-SymbolicName` and `Bundle-Version` are _identical_ then deployment will fail.
 * **NOTE**:  `nocheck` is presently NOT SUPPORTED.

## GET (host)/(context-path)/bundles/(bundle-resource-path)
  * `(bundle-resource-path)` is formed as:  Substitute`.` with `-` in the `Bundle-SymbolicName` (without paramters) and `Bundle-Version` and concatenate them with a `/` in between.
  * Example: `com.sap.it.commons/1-7-0-20131202-1000`
  * If there exists a bundle that has been previously deployed with this resource path, then check the `Accept`.
    ** If no `Accept` header is specified or the header contains `application/json` then return a `BundleResource` JSON object representing this bundle.
    ** If `Accept` header contains `application/jar` or `application/java-archive`, then return the bundle binary with `Content-Type: application/java-archive` and `Content-Disposition: attachment` to force the
       the client to download the bundle.
  * 404 is returned if there is no such bundle resource.

## Bugs and Feature Requests
TODO: fill me in 

