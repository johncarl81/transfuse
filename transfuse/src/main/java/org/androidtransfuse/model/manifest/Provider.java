package org.androidtransfuse.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * attributes:
 * android:authorities="list"
 * android:enabled=["true" | "false"]
 * android:exported=["true" | "false"]
 * android:grantUriPermissions=["true" | "false"]
 * android:icon="drawable resource"
 * android:initOrder="integer"
 * android:label="string resource"
 * android:multiprocess=["true" | "false"]
 * android:name="string"
 * android:permission="string"
 * android:process="string"
 * android:readPermission="string"
 * android:syncable=["true" | "false"]
 * android:writePermission="string"
 * <p/>
 * can contain:
 * <meta-data>
 * <grant-uri-permission>
 * <path-permission>
 *
 * @author John Ericksen
 */
public class Provider {

    @XStreamAlias("android:authorities")
    @XStreamAsAttribute
    private String authorities;
    @XStreamAlias("android:enabled")
    @XStreamAsAttribute
    private Boolean enabled;
    @XStreamAlias("android:exported")
    @XStreamAsAttribute
    private Boolean exported;
    @XStreamAlias("android:grantUriPermissions")
    @XStreamAsAttribute
    private Boolean grantUriPermissions;
    @XStreamAlias("android:icon")
    @XStreamAsAttribute
    private String icon;
    @XStreamAlias("android:initOrder")
    @XStreamAsAttribute
    private Integer initOrder;
    @XStreamAlias("android:label")
    @XStreamAsAttribute
    private String label;
    @XStreamAlias("android:multiprocess")
    @XStreamAsAttribute
    private Boolean multiprocess;
    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("android:permission")
    @XStreamAsAttribute
    private String permission;
    @XStreamAlias("android:process")
    @XStreamAsAttribute
    private String process;
    @XStreamAlias("android:readPermission")
    @XStreamAsAttribute
    private String readPermission;
    @XStreamAlias("android:syncable")
    @XStreamAsAttribute
    private Boolean syncable;
    @XStreamAlias("android:writePermission")
    @XStreamAsAttribute
    private String writePermission;

    @XStreamImplicit(itemFieldName = "meta-data")
    private List<MetaData> metaData = new ArrayList<MetaData>();
    @XStreamImplicit(itemFieldName = "grant-uri-permission")
    private List<GrantUriPermission> grantUriPermissionList = new ArrayList<GrantUriPermission>();
    @XStreamImplicit(itemFieldName = "path-permission")
    private List<PathPermission> pathPermissions = new ArrayList<PathPermission>();

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getExported() {
        return exported;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
    }

    public Boolean getGrantUriPermissions() {
        return grantUriPermissions;
    }

    public void setGrantUriPermissions(Boolean grantUriPermissions) {
        this.grantUriPermissions = grantUriPermissions;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getInitOrder() {
        return initOrder;
    }

    public void setInitOrder(Integer initOrder) {
        this.initOrder = initOrder;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getMultiprocess() {
        return multiprocess;
    }

    public void setMultiprocess(Boolean multiprocess) {
        this.multiprocess = multiprocess;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getReadPermission() {
        return readPermission;
    }

    public void setReadPermission(String readPermission) {
        this.readPermission = readPermission;
    }

    public Boolean getSyncable() {
        return syncable;
    }

    public void setSyncable(Boolean syncable) {
        this.syncable = syncable;
    }

    public String getWritePermission() {
        return writePermission;
    }

    public void setWritePermission(String writePermission) {
        this.writePermission = writePermission;
    }

    public List<MetaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<MetaData> metaData) {
        this.metaData = metaData;
    }

    public List<GrantUriPermission> getGrantUriPermissionList() {
        return grantUriPermissionList;
    }

    public void setGrantUriPermissionList(List<GrantUriPermission> grantUriPermissionList) {
        this.grantUriPermissionList = grantUriPermissionList;
    }

    public List<PathPermission> getPathPermissions() {
        return pathPermissions;
    }

    public void setPathPermissions(List<PathPermission> pathPermissions) {
        this.pathPermissions = pathPermissions;
    }
}
