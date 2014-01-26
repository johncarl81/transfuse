/**
 * Copyright 2013 John Ericksen
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

import org.androidtransfuse.annotations.UIOptions;
import org.androidtransfuse.model.Identified;
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.Merge;
import org.androidtransfuse.processor.MergeCollection;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

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
public class Application extends Mergeable implements Identified {

    private Boolean allowTaskReparenting;
    private String backupAgent;
    private Boolean debuggable;
    private String description;
    private Boolean enabled;
    private Boolean hasCode;
    private Boolean hardwareAccelerated;
    private String icon;
    private Boolean killAfterRestore;
    private String label;
    private String logo;
    private String manageSpaceActivity;
    private String name;
    private String permission;
    private Boolean persistent;
    private String process;
    private Boolean restoreAnyVersion;
    private String taskAffinity;
    private String theme;
    private UIOptions uiOptions;
    private Boolean allowBackup;
    private Boolean largeHeap;
    private Boolean supportsRtl;
    private Boolean restrictedAccountType;
    private Boolean vmSafeMode;
    private Boolean testOnly;
    private String requiredAccountType;
    private List<Activity> activities = new ArrayList<Activity>();
    private List<ActivityAlias> activityAliases = new ArrayList<ActivityAlias>();
    private List<Service> services = new ArrayList<Service>();
    private List<Receiver> receivers = new ArrayList<Receiver>();
    private List<Provider> providers = new ArrayList<Provider>();
    private List<UsesLibrary> usesLibraries = new ArrayList<UsesLibrary>();

    @Merge("r")
    @XmlAttribute(name = "allowTaskReparenting", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getAllowTaskReparenting() {
        return allowTaskReparenting;
    }

    public void setAllowTaskReparenting(Boolean allowTaskReparenting) {
        this.allowTaskReparenting = allowTaskReparenting;
    }

    @Merge("a")
    @XmlAttribute(name = "backupAgent", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getBackupAgent() {
        return backupAgent;
    }

    public void setBackupAgent(String backupAgent) {
        this.backupAgent = backupAgent;
    }

    @Merge("b")
    @XmlAttribute(name = "debuggable", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getDebuggable() {
        return debuggable;
    }

    public void setDebuggable(Boolean debuggable) {
        this.debuggable = debuggable;
    }

    @Merge("d")
    @XmlAttribute(name = "description", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Merge("e")
    @XmlAttribute(name = "enabled", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Merge("c")
    @XmlAttribute(name = "hasCode", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getHasCode() {
        return hasCode;
    }

    public void setHasCode(Boolean hasCode) {
        this.hasCode = hasCode;
    }

    @Merge("h")
    @XmlAttribute(name = "hardwareAccelerated", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getHardwareAccelerated() {
        return hardwareAccelerated;
    }

    public void setHardwareAccelerated(Boolean hardwareAccelerated) {
        this.hardwareAccelerated = hardwareAccelerated;
    }

    @Merge("i")
    @XmlAttribute(name = "icon", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Merge("k")
    @XmlAttribute(name = "killAfterRestore", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getKillAfterRestore() {
        return killAfterRestore;
    }

    public void setKillAfterRestore(Boolean killAfterRestore) {
        this.killAfterRestore = killAfterRestore;
    }

    @Merge("l")
    @XmlAttribute(name = "label", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Merge("o")
    @XmlAttribute(name = "logo", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Merge("s")
    @XmlAttribute(name = "manageSpaceActivity", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getManageSpaceActivity() {
        return manageSpaceActivity;
    }

    public void setManageSpaceActivity(String manageSpaceActivity) {
        this.manageSpaceActivity = manageSpaceActivity;
    }

    @Merge("n")
    @XmlAttribute(name = "name", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Merge("m")
    @XmlAttribute(name = "permission", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Merge("x")
    @XmlAttribute(name = "persistent", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getPersistent() {
        return persistent;
    }

    public void setPersistent(Boolean persistent) {
        this.persistent = persistent;
    }

    @Merge("p")
    @XmlAttribute(name = "process", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    @Merge("v")
    @XmlAttribute(name = "restoreAnyVersion", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getRestoreAnyVersion() {
        return restoreAnyVersion;
    }

    public void setRestoreAnyVersion(Boolean restoreAnyVersion) {
        this.restoreAnyVersion = restoreAnyVersion;
    }

    @Merge("f")
    @XmlAttribute(name = "taskAffinity", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getTaskAffinity() {
        return taskAffinity;
    }

    public void setTaskAffinity(String taskAffinity) {
        this.taskAffinity = taskAffinity;
    }

    @Merge("t")
    @XmlAttribute(name = "theme", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @Merge("u")
    @XmlAttribute(name = "uiOptions", namespace = ManifestNamespaceMapper.ANDROID_URI)
    @XmlJavaTypeAdapter(LabeledConverter.UIOptionsConverter.class)
    public UIOptions getUiOptions() {
        return uiOptions;
    }

    public void setUiOptions(UIOptions uiOptions) {
        this.uiOptions = uiOptions;
    }

    @MergeCollection(collectionType = ArrayList.class, type = Activity.class)
    @XmlElement(name = "activity")
    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    @XmlElement(name = "activity-alias")
    public List<ActivityAlias> getActivityAliases() {
        return activityAliases;
    }

    public void setActivityAliases(List<ActivityAlias> activityAliases) {
        this.activityAliases = activityAliases;
    }

    @MergeCollection(collectionType = ArrayList.class, type = Service.class)
    @XmlElement(name = "service")
    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    @MergeCollection(collectionType = ArrayList.class, type = Receiver.class)
    @XmlElement(name = "receiver")
    public List<Receiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Receiver> receivers) {
        this.receivers = receivers;
    }

    @XmlElement(name = "provider")
    public List<Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

    @XmlElement(name = "uses-library")
    public List<UsesLibrary> getUsesLibraries() {
        return usesLibraries;
    }

    public void setUsesLibraries(List<UsesLibrary> usesLibraries) {
        this.usesLibraries = usesLibraries;
    }

    @Merge("ab")
    @XmlAttribute(name = "allowBackup", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getAllowBackup() {
        return allowBackup;
    }

    public void setAllowBackup(Boolean allowBackup) {
        this.allowBackup = allowBackup;
    }

    @Merge("lh")
    @XmlAttribute(name = "largeHeap", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getLargeHeap() {
        return largeHeap;
    }

    public void setLargeHeap(Boolean largeHeap) {
        this.largeHeap = largeHeap;
    }

    @Merge("rt")
    @XmlAttribute(name = "supportsRtl", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getSupportsRtl() {
        return supportsRtl;
    }

    public void setSupportsRtl(Boolean supportsRtl) {
        this.supportsRtl = supportsRtl;
    }

    @Merge("ra")
    @XmlAttribute(name = "restrictedAccountType", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getRestrictedAccountType() {
        return restrictedAccountType;
    }

    public void setRestrictedAccountType(Boolean restrictedAccountType) {
        this.restrictedAccountType = restrictedAccountType;
    }

    @Merge("vm")
    @XmlAttribute(name = "vmSafeMode", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getVmSafeMode() {
        return vmSafeMode;
    }

    public void setVmSafeMode(Boolean vmSafeMode) {
        this.vmSafeMode = vmSafeMode;
    }

    @Merge("to")
    @XmlAttribute(name = "testOnly", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getTestOnly() {
        return testOnly;
    }

    public void setTestOnly(Boolean testOnly) {
        this.testOnly = testOnly;
    }

    @Merge("rc")
    @XmlAttribute(name = "requiredAccountType", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getRequiredAccountType() {
        return requiredAccountType;
    }

    public void setRequiredAccountType(String requiredAccountType) {
        this.requiredAccountType = requiredAccountType;
    }

    @Override
    @XmlTransient
    public String getIdentifier() {
        return name;
    }

    public void updatePackage(String manifestPackage) {
        if(name != null && name.startsWith(manifestPackage) && containsTag("n")){
            name = name.substring(manifestPackage.length());
        }

        if(activities != null){
            for (Activity activity : activities) {
                activity.updatePackage(manifestPackage);
            }
        }

        if(services != null){
            for (Service service : services) {
                service.updatePackage(manifestPackage);
            }
        }

        if(receivers != null){
            for (Receiver receiver : receivers) {
                receiver.updatePackage(manifestPackage);
            }
        }
    }
}
