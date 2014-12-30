/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.model.manifest;

import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.MergeCollection;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * attributes:
 * xmlns:android="http://schemas.android.com/apk/res/android"
 * package="string"
 * android:sharedUserId="string"
 * android:sharedUserLabel="string resource"
 * android:versionCode="integer"
 * android:versionName="string"
 * android:installLocation=["auto" | "internalOnly" | "preferExternal"]
 *
 * must contain:
 * <application>
 *
 * can contain:
 * <instrumentation>
 * <permission>
 * <permission-group>
 * <permission-tree>
 * <uses-configuration>
 * <uses-permission>
 * <uses-sdk>
 * <compatible-screens>
 *
 * @author John Ericksen
 */
@XmlRootElement(name = "manifest")
@XmlType(propOrder = {
        // root manifest element attributes
        "applicationPackage",
        "sharedUserId",
        "sharedUserLabel",
        "versionCode",
        "versionName",
        "installLocation",
        // top level elements
        "usesPermissions",
        "permissions",
        "permissionTrees",
        "permissionGroups",
        "instrumentations",
        "usesSDKs",
        "usesConfigurations",
        "usesFeatures",
        "supportsScreens",
        "compatibleScreens",
        "applications"
        }
)
public class Manifest extends Mergeable {

    private String applicationPackage;
    private String sharedUserId;
    private String sharedUserLabel;
    private Integer versionCode;
    private String versionName;
    private InstallLocation installLocation;
    private List<UsesPermission> usesPermissions = new ArrayList<UsesPermission>();
    private List<UsesSDK> usesSDKs = new ArrayList<UsesSDK>();
    private List<Application> applications = new ArrayList<Application>();
    private List<Instrumentation> instrumentations = new ArrayList<Instrumentation>();
    private List<Permission> permissions = new ArrayList<Permission>();
    private List<PermissionGroup> permissionGroups = new ArrayList<PermissionGroup>();
    private List<PermissionTree> permissionTrees = new ArrayList<PermissionTree>();
    private List<SupportsScreens> supportsScreens = new ArrayList<SupportsScreens>();
    private List<UsesFeature> usesFeatures = new ArrayList<UsesFeature>();
    private List<UsesConfiguration> usesConfigurations = new ArrayList<UsesConfiguration>();
    private List<CompatibleScreens> compatibleScreens = new ArrayList<CompatibleScreens>();

    @XmlAttribute(name = "package")
    public String getApplicationPackage() {
        return applicationPackage;
    }

    public void setApplicationPackage(String applicationPackage) {
        this.applicationPackage = applicationPackage;
    }

    @XmlAttribute(name = "sharedUserId", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getSharedUserId() {
        return sharedUserId;
    }

    public void setSharedUserId(String sharedUserId) {
        this.sharedUserId = sharedUserId;
    }

    @XmlAttribute(name = "sharedUserLabel", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getSharedUserLabel() {
        return sharedUserLabel;
    }

    public void setSharedUserLabel(String sharedUserLabel) {
        this.sharedUserLabel = sharedUserLabel;
    }

    @XmlAttribute(name = "versionCode", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    @XmlAttribute(name = "versionName", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @XmlAttribute(name = "installLocation", namespace = ManifestNamespaceMapper.ANDROID_URI)
    @XmlJavaTypeAdapter(LabeledConverter.InstallLocationConverter.class)
    public InstallLocation getInstallLocation() {
        return installLocation;
    }

    public void setInstallLocation(InstallLocation installLocation) {
        this.installLocation = installLocation;
    }

    @MergeCollection(collectionType = ArrayList.class, type = Application.class)
    @XmlElement(name = "application")
    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    @XmlElement(name = "instrumentation")
    public List<Instrumentation> getInstrumentations() {
        return instrumentations;
    }

    public void setInstrumentations(List<Instrumentation> instrumentations) {
        this.instrumentations = instrumentations;
    }

    @XmlElement(name = "permission")
    public List<Permission> getPermissions() {
        return permissions;
    }

    @MergeCollection(collectionType = ArrayList.class, type = Permission.class)
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @XmlElement(name = "permission-group")
    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public void setPermissionGroups(List<PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
    }

    @XmlElement(name = "permission-tree")
    public List<PermissionTree> getPermissionTrees() {
        return permissionTrees;
    }

    public void setPermissionTrees(List<PermissionTree> permissionTrees) {
        this.permissionTrees = permissionTrees;
    }

    @XmlElement(name = "uses-configuration")
    public List<UsesConfiguration> getUsesConfigurations() {
        return usesConfigurations;
    }

    public void setUsesConfigurations(List<UsesConfiguration> usesConfigurations) {
        this.usesConfigurations = usesConfigurations;
    }

    @MergeCollection(collectionType = ArrayList.class, type = UsesPermission.class)
    @XmlElement(name = "uses-permission")
    public List<UsesPermission> getUsesPermissions() {
        return usesPermissions;
    }

    public void setUsesPermissions(List<UsesPermission> usesPermissions) {
        this.usesPermissions = usesPermissions;
    }

    @MergeCollection(collectionType = ArrayList.class, type = UsesSDK.class)
    @XmlElement(name = "uses-sdk")
    public List<UsesSDK> getUsesSDKs() {
        return usesSDKs;
    }

    public void setUsesSDKs(List<UsesSDK> usesSDKs) {
        this.usesSDKs = usesSDKs;
    }

    @XmlElement(name = "supports-screens")
    public List<SupportsScreens> getSupportsScreens() {
        return supportsScreens;
    }

    public void setSupportsScreens(List<SupportsScreens> supportsScreens) {
        this.supportsScreens = supportsScreens;
    }

    @MergeCollection(collectionType = ArrayList.class, type = UsesFeature.class)
    @XmlElement(name = "uses-feature")
    public List<UsesFeature> getUsesFeatures() {
        return usesFeatures;
    }

    public void setUsesFeatures(List<UsesFeature> usesFeatures) {
        this.usesFeatures = usesFeatures;
    }

    @XmlElement(name = "compatible-screens")
    public List<CompatibleScreens> getCompatibleScreens() {
        return compatibleScreens;
    }

    public void setCompatibleScreens(List<CompatibleScreens> compatibleScreens) {
        this.compatibleScreens = compatibleScreens;
    }

    public void updatePackages() {
        if(applicationPackage != null){
            for (Application application : applications) {
                application.updatePackage(applicationPackage);
            }
        }
    }
}
