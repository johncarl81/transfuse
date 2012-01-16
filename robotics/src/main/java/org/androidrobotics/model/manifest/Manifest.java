package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * @author John Ericksen
 */
@XStreamAlias("manifest")
public class Manifest {

    /*
       must contain:
       <application>
       can contain:
       <instrumentation>
       <permission>
       <permission-group>
       <permission-tree>
       <uses-configuration>
       <uses-permission>

       <uses-sdk>
    */

    /*
        xmlns:android="http://schemas.android.com/apk/res/android"
        package="string"
        android:sharedUserId="string"
        android:sharedUserLabel="string resource"
        android:versionCode="integer"
        android:versionName="string"
        android:installLocation=["auto" | "internalOnly" | "preferExternal"]
     */

    @XStreamAlias("package")
    @XStreamAsAttribute
    private String applicationPackage;
    @XStreamAlias("android:sharedUserId")
    @XStreamAsAttribute
    private String sharedUserId;
    @XStreamAlias("android:sharedUserLabel")
    @XStreamAsAttribute
    private String sharedUserLabel;
    @XStreamAlias("android:versionCode")
    @XStreamAsAttribute
    private Integer versionCode;
    @XStreamAlias("android:versionName")
    @XStreamAsAttribute
    private String versionName;
    @XStreamAlias("android:installLocation")
    @XStreamAsAttribute
    private InstallLocation installLocation;

    @XStreamImplicit(itemFieldName = "uses-permission")
    private List<UsesPermission> usesPermissions;

    /*
    private Application application;
    private List<Instrumentation> instrumentations;
    private List<Permission> permissions;
    private List<PermissionGroup> permissionGroups;
    private List<PermissionTree> permissionTrees;
    private List<UsesConfiguration> usesConfigurations;
    private List<UsesSDK> usesSDKs;
    */

    public String getApplicationPackage() {
        return applicationPackage;
    }

    public void setApplicationPackage(String applicationPackage) {
        this.applicationPackage = applicationPackage;
    }

    public String getSharedUserId() {
        return sharedUserId;
    }

    public void setSharedUserId(String sharedUserId) {
        this.sharedUserId = sharedUserId;
    }

    public String getSharedUserLabel() {
        return sharedUserLabel;
    }

    public void setSharedUserLabel(String sharedUserLabel) {
        this.sharedUserLabel = sharedUserLabel;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public InstallLocation getInstallLocation() {
        return installLocation;
    }

    public void setInstallLocation(InstallLocation installLocation) {
        this.installLocation = installLocation;
    }

    public List<UsesPermission> getUsesPermissions() {
        return usesPermissions;
    }
}
