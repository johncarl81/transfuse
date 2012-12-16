/**
 * Copyright 2012 John Ericksen
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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.androidtransfuse.annotations.UIOptions;
import org.androidtransfuse.model.Identified;
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.Merge;
import org.androidtransfuse.processor.MergeCollection;

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
@XStreamAlias("application")
public class Application extends Mergeable implements Identified {

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

    @XStreamImplicit(itemFieldName = "activity")
    private List<Activity> activities = new ArrayList<Activity>();
    @XStreamImplicit(itemFieldName = "activity-alias")
    private List<ActivityAlias> activityAliases = new ArrayList<ActivityAlias>();
    @XStreamImplicit(itemFieldName = "service")
    private List<Service> services = new ArrayList<Service>();
    @XStreamImplicit(itemFieldName = "receiver")
    private List<Receiver> receivers = new ArrayList<Receiver>();
    @XStreamImplicit(itemFieldName = "provider")
    private List<Provider> providers = new ArrayList<Provider>();
    @XStreamImplicit(itemFieldName = "uses-library")
    private List<UsesLibrary> usesLibraries = new ArrayList<UsesLibrary>();

    @Merge("r")
    public Boolean getAllowTaskReparenting() {
        return allowTaskReparenting;
    }

    public void setAllowTaskReparenting(Boolean allowTaskReparenting) {
        this.allowTaskReparenting = allowTaskReparenting;
    }

    @Merge("a")
    public String getBackupAgent() {
        return backupAgent;
    }

    public void setBackupAgent(String backupAgent) {
        this.backupAgent = backupAgent;
    }

    @Merge("b")
    public Boolean getDebuggable() {
        return debuggable;
    }

    public void setDebuggable(Boolean debuggable) {
        this.debuggable = debuggable;
    }

    @Merge("d")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Merge("e")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Merge("c")
    public Boolean getHasCode() {
        return hasCode;
    }

    public void setHasCode(Boolean hasCode) {
        this.hasCode = hasCode;
    }

    @Merge("h")
    public Boolean getHardwareAccelerated() {
        return hardwareAccelerated;
    }

    public void setHardwareAccelerated(Boolean hardwareAccelerated) {
        this.hardwareAccelerated = hardwareAccelerated;
    }

    @Merge("i")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Merge("k")
    public Boolean getKillAfterRestore() {
        return killAfterRestore;
    }

    public void setKillAfterRestore(Boolean killAfterRestore) {
        this.killAfterRestore = killAfterRestore;
    }

    @Merge("l")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Merge("o")
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Merge("s")
    public String getManageSpaceActivity() {
        return manageSpaceActivity;
    }

    public void setManageSpaceActivity(String manageSpaceActivity) {
        this.manageSpaceActivity = manageSpaceActivity;
    }

    @Merge("n")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Merge("m")
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Merge("x")
    public Boolean getPersistent() {
        return persistent;
    }

    public void setPersistent(Boolean persistent) {
        this.persistent = persistent;
    }

    @Merge("p")
    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    @Merge("v")
    public Boolean getRestoreAnyVersion() {
        return restoreAnyVersion;
    }

    public void setRestoreAnyVersion(Boolean restoreAnyVersion) {
        this.restoreAnyVersion = restoreAnyVersion;
    }

    @Merge("f")
    public String getTaskAffinity() {
        return taskAffinity;
    }

    public void setTaskAffinity(String taskAffinity) {
        this.taskAffinity = taskAffinity;
    }

    @Merge("t")
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @Merge("u")
    public UIOptions getUiOptions() {
        return uiOptions;
    }

    public void setUiOptions(UIOptions uiOptions) {
        this.uiOptions = uiOptions;
    }

    @MergeCollection(collectionType = ArrayList.class, type = Activity.class)
    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<ActivityAlias> getActivityAliases() {
        return activityAliases;
    }

    public void setActivityAliases(List<ActivityAlias> activityAliases) {
        this.activityAliases = activityAliases;
    }

    @MergeCollection(collectionType = ArrayList.class, type = Service.class)
    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    @MergeCollection(collectionType = ArrayList.class, type = Receiver.class)
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
