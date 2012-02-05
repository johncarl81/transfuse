package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.androidrobotics.processor.Mergable;
import org.androidrobotics.processor.MergeCollection;

import java.util.List;
import java.util.Set;

/**
 * attributes
 * android:allowTaskReparenting=["true" | "false"]
 * android:backupAgent="string"
 * android:debuggable=["true" | "false"]
 * android:description="string resource"
 * android:enabled=["true" | "false"]
 * android:hasCode=["true" | "false"]
 * android:hardwareAccelerated=["true" | "false"]
 * android:icon="drawable resource"
 * android:killAfterRestore=["true" | "false"]
 * android:label="string resource"
 * android:logo="drawable resource"
 * android:manageSpaceActivity="string"
 * android:name="string"
 * android:permission="string"
 * android:persistent=["true" | "false"]
 * android:process="string"
 * android:restoreAnyVersion=["true" | "false"]
 * android:taskAffinity="string"
 * android:theme="resource or theme"
 * android:uiOptions=["none" | "splitActionBarWhenNarrow"]
 * <p/>
 * can contain:
 * <activity>
 * <activity-alias>
 * <service>
 * <receiver>
 * <provider>
 * <uses-library>
 *
 * @author John Ericksen
 */
@XStreamAlias("application")
public class Application implements Mergable<String> {

    @XStreamAlias("android:allowTaskReparenting")
    @XStreamAsAttribute
    private Boolean allowTaskReparenting;
    @XStreamAlias("android:backupAgent")
    @XStreamAsAttribute
    private String backupAgent;
    @XStreamAlias("android:debuggable")
    @XStreamAsAttribute
    private Boolean debuggable;
    @XStreamAlias("android:description")
    @XStreamAsAttribute
    private String description;
    @XStreamAlias("android:enabled")
    @XStreamAsAttribute
    private Boolean enabled;
    @XStreamAlias("android:hasCode")
    @XStreamAsAttribute
    private Boolean hasCode;
    @XStreamAlias("android:hardwareAccelerated")
    @XStreamAsAttribute
    private Boolean hardwareAccelerated;
    @XStreamAlias("android:icon")
    @XStreamAsAttribute
    private String icon;
    @XStreamAlias("android:killAfterRestore")
    @XStreamAsAttribute
    private Boolean killAfterRestore;
    @XStreamAlias("android:label")
    @XStreamAsAttribute
    private String label;
    @XStreamAlias("android:logo")
    @XStreamAsAttribute
    private String logo;
    @XStreamAlias("android:manageSpaceActivity")
    @XStreamAsAttribute
    private String manageSpaceActivity;
    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("android:permission")
    @XStreamAsAttribute
    private String permission;
    @XStreamAlias("android:persistent")
    @XStreamAsAttribute
    private Boolean persistent;
    @XStreamAlias("android:process")
    @XStreamAsAttribute
    private String process;
    @XStreamAlias("android:restoreAnyVersion")
    @XStreamAsAttribute
    private Boolean restoreAnyVersion;
    @XStreamAlias("android:taskAffinity")
    @XStreamAsAttribute
    private String taskAffinity;
    @XStreamAlias("android:theme")
    @XStreamAsAttribute
    private String theme;
    @XStreamAlias("android:uiOptions")
    @XStreamAsAttribute
    private UIOptions uiOptions;
    @XStreamAlias("transfuse")
    @XStreamAsAttribute
    private String tag;

    @XStreamImplicit(itemFieldName = "activity")
    @MergeCollection
    private Set<Activity> activities;
    @XStreamImplicit(itemFieldName = "activity-alias")
    private List<ActivityAlias> activityAliases;
    @XStreamImplicit(itemFieldName = "service")
    private List<Service> services;
    @XStreamImplicit(itemFieldName = "receiver")
    private List<Receiver> receivers;
    @XStreamImplicit(itemFieldName = "provider")
    private List<Provider> providers;
    @XStreamImplicit(itemFieldName = "uses-library")
    private List<UsesLibrary> usesLibraries;

    public Boolean getAllowTaskReparenting() {
        return allowTaskReparenting;
    }

    public void setAllowTaskReparenting(Boolean allowTaskReparenting) {
        this.allowTaskReparenting = allowTaskReparenting;
    }

    public String getBackupAgent() {
        return backupAgent;
    }

    public void setBackupAgent(String backupAgent) {
        this.backupAgent = backupAgent;
    }

    public Boolean getDebuggable() {
        return debuggable;
    }

    public void setDebuggable(Boolean debuggable) {
        this.debuggable = debuggable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getHasCode() {
        return hasCode;
    }

    public void setHasCode(Boolean hasCode) {
        this.hasCode = hasCode;
    }

    public Boolean getHardwareAccelerated() {
        return hardwareAccelerated;
    }

    public void setHardwareAccelerated(Boolean hardwareAccelerated) {
        this.hardwareAccelerated = hardwareAccelerated;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getKillAfterRestore() {
        return killAfterRestore;
    }

    public void setKillAfterRestore(Boolean killAfterRestore) {
        this.killAfterRestore = killAfterRestore;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getManageSpaceActivity() {
        return manageSpaceActivity;
    }

    public void setManageSpaceActivity(String manageSpaceActivity) {
        this.manageSpaceActivity = manageSpaceActivity;
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

    public Boolean getPersistent() {
        return persistent;
    }

    public void setPersistent(Boolean persistent) {
        this.persistent = persistent;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Boolean getRestoreAnyVersion() {
        return restoreAnyVersion;
    }

    public void setRestoreAnyVersion(Boolean restoreAnyVersion) {
        this.restoreAnyVersion = restoreAnyVersion;
    }

    public String getTaskAffinity() {
        return taskAffinity;
    }

    public void setTaskAffinity(String taskAffinity) {
        this.taskAffinity = taskAffinity;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public UIOptions getUiOptions() {
        return uiOptions;
    }

    public void setUiOptions(UIOptions uiOptions) {
        this.uiOptions = uiOptions;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public List<ActivityAlias> getActivityAliases() {
        return activityAliases;
    }

    public void setActivityAliases(List<ActivityAlias> activityAliases) {
        this.activityAliases = activityAliases;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Receiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Receiver> receivers) {
        this.receivers = receivers;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

    public List<UsesLibrary> getUsesLibraries() {
        return usesLibraries;
    }

    public void setUsesLibraries(List<UsesLibrary> usesLibraries) {
        this.usesLibraries = usesLibraries;
    }

    @Override
    public String getIdentifier() {
        return name;
    }

    @Override
    public void setMergeTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String getMergeTag() {
        return tag;
    }
}
