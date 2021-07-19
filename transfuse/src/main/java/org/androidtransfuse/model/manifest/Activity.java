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

import org.androidtransfuse.annotations.LaunchMode;
import org.androidtransfuse.annotations.ScreenOrientation;
import org.androidtransfuse.annotations.UIOptions;
import org.androidtransfuse.annotations.WindowSoftInputMode;
import org.androidtransfuse.model.Identified;
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.Merge;
import org.androidtransfuse.processor.MergeCollection;
import org.apache.commons.lang.builder.EqualsBuilder;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * attributes:
 * android:allowTaskReparenting=["true" | "false"]
 * android:alwaysRetainTaskState=["true" | "false"]
 * android:clearTaskOnLaunch=["true" | "false"]
 * android:configChanges=["mcc", "mnc", "locale",
 * "touchscreen", "keyboard", "keyboardHidden",
 * "navigation", "screenLayout", "fontScale", "uiMode",
 * "orientation", "screenSize", "smallestScreenSize"]
 * android:enabled=["true" | "false"]
 * android:excludeFromRecents=["true" | "false"]
 * android:exported=["true" | "false"]
 * android:finishOnTaskLaunch=["true" | "false"]
 * android:hardwareAccelerated=["true" | "false"]
 * android:icon="drawable resource"
 * android:label="string resource"
 * android:launchMode=["multiple" | "singleTop" |
 * "singleTask" | "singleInstance"]
 * android:multiprocess=["true" | "false"]
 * android:name="string"
 * android:noHistory=["true" | "false"]
 * android:permission="string"
 * android:process="string"
 * android:screenOrientation=["unspecified" | "user" | "behind" |
 * "landscape" | "portrait" |
 * "reverseLandscape" | "reversePortrait" |
 * "sensorLandscape" | "sensorPortrait" |
 * "sensor" | "fullSensor" | "nosensor"]
 * android:stateNotNeeded=["true" | "false"]
 * android:taskAffinity="string"
 * android:theme="resource or theme"
 * android:uiOptions=["none" | "splitActionBarWhenNarrow"]
 * android:windowSoftInputMode=["stateUnspecified",
 * "stateUnchanged", "stateHidden",
 * "stateAlwaysHidden", "stateVisible",
 * "stateAlwaysVisible", "adjustUnspecified",
 * "adjustResize", "adjustPan"]
 *
 * can contain:
 * <intent-filter>
 * <meta-data>
 *
 * @author John Ericksen
 */
public class Activity extends Mergeable implements Comparable<Activity>, Identified {

    private Boolean allowTaskReparenting;
    private Boolean alwaysRetainTaskState;
    private Boolean clearTaskOnLaunch;
    private String configChanges;
    private Boolean enabled;
    private Boolean excludeFromRecents;
    private Boolean exported;
    private Boolean finishOnTaskLaunch;
    private Boolean hardwareAccelerated;
    private String icon;
    private String label;
    private LaunchMode launchMode;
    private Boolean multiprocess;
    private String name;
    private Boolean noHistory;
    private String permission;
    private String process;
    private ScreenOrientation screenOrientation;
    private Boolean stateNotNeeded;
    private String taskAffinity;
    private String theme;
    private UIOptions uiOptions;
    private WindowSoftInputMode windowSoftInputMode;
    private List<IntentFilter> intentFilters = new ArrayList<IntentFilter>();
    private List<MetaData> metaData = new ArrayList<MetaData>();

    @Merge("t")
    @XmlAttribute(name = "allowTaskReparenting", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getAllowTaskReparenting() {
        return allowTaskReparenting;
    }

    public void setAllowTaskReparenting(Boolean allowTaskReparenting) {
        this.allowTaskReparenting = allowTaskReparenting;
    }

    @Merge("s")
    @XmlAttribute(name = "alwaysRetainTaskState", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getAlwaysRetainTaskState() {
        return alwaysRetainTaskState;
    }

    public void setAlwaysRetainTaskState(Boolean alwaysRetainTaskState) {
        this.alwaysRetainTaskState = alwaysRetainTaskState;
    }

    @Merge("c")
    @XmlAttribute(name = "clearTaskOnLaunch", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getClearTaskOnLaunch() {
        return clearTaskOnLaunch;
    }

    public void setClearTaskOnLaunch(Boolean clearTaskOnLaunch) {
        this.clearTaskOnLaunch = clearTaskOnLaunch;
    }

    @Merge("a")
    @XmlAttribute(name = "configChanges", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getConfigChanges() {
        return configChanges;
    }

    public void setConfigChanges(String configChanges) {
        this.configChanges = configChanges;
    }

    @Merge("e")
    @XmlAttribute(name = "enabled", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Merge("r")
    @XmlAttribute(name = "excludeFromRecents", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getExcludeFromRecents() {
        return excludeFromRecents;
    }

    public void setExcludeFromRecents(Boolean excludeFromRecents) {
        this.excludeFromRecents = excludeFromRecents;
    }

    @Merge("x")
    @XmlAttribute(name = "exported", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getExported() {
        return exported;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
    }

    @Merge("f")
    @XmlAttribute(name = "finishOnTaskLaunch", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getFinishOnTaskLaunch() {
        return finishOnTaskLaunch;
    }

    public void setFinishOnTaskLaunch(Boolean finishOnTaskLaunch) {
        this.finishOnTaskLaunch = finishOnTaskLaunch;
    }

    @Merge("j")
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

    @Merge("l")
    @XmlAttribute(name = "label", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Merge("u")
    @XmlAttribute(name = "launchMode", namespace = ManifestNamespaceMapper.ANDROID_URI)
    @XmlJavaTypeAdapter(LabeledConverter.LaunchModeConverter.class)
    public LaunchMode getLaunchMode() {
        return launchMode;
    }

    public void setLaunchMode(LaunchMode launchMode) {
        this.launchMode = launchMode;
    }

    @Merge("m")
    @XmlAttribute(name = "multiprocess", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getMultiprocess() {
        return multiprocess;
    }

    public void setMultiprocess(Boolean multiprocess) {
        this.multiprocess = multiprocess;
    }

    @Merge("n")
    @XmlAttribute(name = "name", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Merge("v")
    @XmlAttribute(name = "noHistory", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getNoHistory() {
        return noHistory;
    }

    public void setNoHistory(Boolean noHistory) {
        this.noHistory = noHistory;
    }

    @Merge("p")
    @XmlAttribute(name = "permission", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Merge("o")
    @XmlAttribute(name = "process", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    @Merge("g")
    @XmlAttribute(name = "screenOrientation", namespace = ManifestNamespaceMapper.ANDROID_URI)
    @XmlJavaTypeAdapter(LabeledConverter.ScreenOrientationConverter.class)
    public ScreenOrientation getScreenOrientation() {
        return screenOrientation;
    }

    public void setScreenOrientation(ScreenOrientation screenOrientation) {
        this.screenOrientation = screenOrientation;
    }

    @Merge("d")
    @XmlAttribute(name = "stateNotNeeded", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getStateNotNeeded() {
        return stateNotNeeded;
    }

    public void setStateNotNeeded(Boolean stateNotNeeded) {
        this.stateNotNeeded = stateNotNeeded;
    }

    @Merge("y")
    @XmlAttribute(name = "taskAffinity", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getTaskAffinity() {
        return taskAffinity;
    }

    public void setTaskAffinity(String taskAffinity) {
        this.taskAffinity = taskAffinity;
    }

    @Merge("h")
    @XmlAttribute(name = "theme", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @Merge("b")
    @XmlAttribute(name = "uiOptions", namespace = ManifestNamespaceMapper.ANDROID_URI)
    @XmlJavaTypeAdapter(LabeledConverter.UIOptionsConverter.class)
    public UIOptions getUiOptions() {
        return uiOptions;
    }

    public void setUiOptions(UIOptions uiOptions) {
        this.uiOptions = uiOptions;
    }

    @Merge("w")
    @XmlAttribute(name = "windowSoftInputMode", namespace = ManifestNamespaceMapper.ANDROID_URI)
    @XmlJavaTypeAdapter(LabeledConverter.WindowSoftInputModeConverter.class)
    public WindowSoftInputMode getWindowSoftInputMode() {
        return windowSoftInputMode;
    }

    public void setWindowSoftInputMode(WindowSoftInputMode windowSoftInputMode) {
        this.windowSoftInputMode = windowSoftInputMode;
    }

    @MergeCollection(collectionType = ArrayList.class, type = IntentFilter.class)
    @XmlElement(name = "intent-filter")
    public List<IntentFilter> getIntentFilters() {
        return intentFilters;
    }

    public void setIntentFilters(List<IntentFilter> intentFilters) {
        this.intentFilters = intentFilters;
    }

    @MergeCollection(collectionType = ArrayList.class, type = MetaData.class)
    @XmlElement(name = "meta-data")
    public List<MetaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<MetaData> metaData) {
        this.metaData = metaData;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Activity)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Activity rhs = (Activity) obj;
        return new EqualsBuilder()
                .append(name, rhs.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    @XmlTransient
    public String getIdentifier() {
        return name;
    }

    @Override
    public int compareTo(Activity activity) {
        return getName().compareTo(activity.getName());
    }

    public void updatePackage(String manifestPackage){
        if(name != null && name.startsWith(manifestPackage) && containsTag("n")){
            name = name.substring(manifestPackage.length());
        }
    }
}
