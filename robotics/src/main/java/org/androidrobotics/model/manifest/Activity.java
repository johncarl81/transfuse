package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.androidrobotics.processor.Merge;
import org.androidrobotics.processor.MergeCollection;
import org.androidrobotics.processor.Mergeable;

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
 * <p/>
 * can contain:
 * <intent-filter>
 * <meta-data>
 *
 * @author John Ericksen
 */
public class Activity extends Mergeable<String> {

    @XStreamAlias("android:allowTaskReparenting")
    @XStreamAsAttribute
    private Boolean allowTaskReparenting;
    @XStreamAlias("android:alwaysRetainTaskState")
    @XStreamAsAttribute
    private Boolean alwaysRetainTaskState;
    @XStreamAlias("android:clearTaskOnLaunch")
    @XStreamAsAttribute
    private Boolean clearTaskOnLaunch;
    @XStreamAlias("android:configChanges")
    @XStreamAsAttribute
    private ConfigChanges configChanges;
    @XStreamAlias("android:enabled")
    @XStreamAsAttribute
    private Boolean enabled;
    @XStreamAlias("android:excludeFromRecents")
    @XStreamAsAttribute
    private Boolean excludeFromRecents;
    @XStreamAlias("android:exported")
    @XStreamAsAttribute
    private Boolean exported;
    @XStreamAlias("android:finishOnTaskLaunch")
    @XStreamAsAttribute
    private Boolean finishOnTaskLaunch;
    @XStreamAlias("android:hardwareAccelerated")
    @XStreamAsAttribute
    private Boolean hardwareAccelerated;
    @XStreamAlias("android:icon")
    @XStreamAsAttribute
    private String icon;
    @XStreamAlias("android:label")
    @XStreamAsAttribute
    @Merge
    private String label;
    @XStreamAlias("android:launchMode")
    @XStreamAsAttribute
    private LaunchMode launchMode;
    @XStreamAlias("android:multiprocess")
    @XStreamAsAttribute
    private Boolean multiprocess;
    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("android:noHistory")
    @XStreamAsAttribute
    private Boolean noHistory;
    @XStreamAlias("android:permission")
    @XStreamAsAttribute
    private String permission;
    @XStreamAlias("android:process")
    @XStreamAsAttribute
    private String process;
    @XStreamAlias("android:screenOrientation")
    @XStreamAsAttribute
    private ScreenOrientation screenOrientation;
    @XStreamAlias("android:stateNotNeeded")
    @XStreamAsAttribute
    private Boolean stateNotNeeded;
    @XStreamAlias("android:taskAffinity")
    @XStreamAsAttribute
    private String taskAffinity;
    @XStreamAlias("android:theme")
    @XStreamAsAttribute
    private String theme;
    @XStreamAlias("android:uiOptions")
    @XStreamAsAttribute
    private UIOptions uiOptions;
    @XStreamAlias("android:windowSoftInputMode")
    @XStreamAsAttribute
    private WindowSoftInputMode windowSoftInputMode;

    @XStreamImplicit(itemFieldName = "intent-filter")
    @MergeCollection(targetType = ArrayList.class)
    private List<IntentFilter> intentFilters = new ArrayList<IntentFilter>();
    @XStreamImplicit(itemFieldName = "meta-data")
    private List<MetaData> metaData = new ArrayList<MetaData>();

    public Activity(String name, String label) {
        this.name = name;
        this.label = label;
    }

    public Boolean getAllowTaskReparenting() {
        return allowTaskReparenting;
    }

    public void setAllowTaskReparenting(Boolean allowTaskReparenting) {
        this.allowTaskReparenting = allowTaskReparenting;
    }

    public Boolean getAlwaysRetainTaskState() {
        return alwaysRetainTaskState;
    }

    public void setAlwaysRetainTaskState(Boolean alwaysRetainTaskState) {
        this.alwaysRetainTaskState = alwaysRetainTaskState;
    }

    public Boolean getClearTaskOnLaunch() {
        return clearTaskOnLaunch;
    }

    public void setClearTaskOnLaunch(Boolean clearTaskOnLaunch) {
        this.clearTaskOnLaunch = clearTaskOnLaunch;
    }

    public ConfigChanges getConfigChanges() {
        return configChanges;
    }

    public void setConfigChanges(ConfigChanges configChanges) {
        this.configChanges = configChanges;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getExcludeFromRecents() {
        return excludeFromRecents;
    }

    public void setExcludeFromRecents(Boolean excludeFromRecents) {
        this.excludeFromRecents = excludeFromRecents;
    }

    public Boolean getExported() {
        return exported;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
    }

    public Boolean getFinishOnTaskLaunch() {
        return finishOnTaskLaunch;
    }

    public void setFinishOnTaskLaunch(Boolean finishOnTaskLaunch) {
        this.finishOnTaskLaunch = finishOnTaskLaunch;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LaunchMode getLaunchMode() {
        return launchMode;
    }

    public void setLaunchMode(LaunchMode launchMode) {
        this.launchMode = launchMode;
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

    public Boolean getNoHistory() {
        return noHistory;
    }

    public void setNoHistory(Boolean noHistory) {
        this.noHistory = noHistory;
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

    public ScreenOrientation getScreenOrientation() {
        return screenOrientation;
    }

    public void setScreenOrientation(ScreenOrientation screenOrientation) {
        this.screenOrientation = screenOrientation;
    }

    public Boolean getStateNotNeeded() {
        return stateNotNeeded;
    }

    public void setStateNotNeeded(Boolean stateNotNeeded) {
        this.stateNotNeeded = stateNotNeeded;
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

    public WindowSoftInputMode getWindowSoftInputMode() {
        return windowSoftInputMode;
    }

    public void setWindowSoftInputMode(WindowSoftInputMode windowSoftInputMode) {
        this.windowSoftInputMode = windowSoftInputMode;
    }

    public List<IntentFilter> getIntentFilters() {
        return intentFilters;
    }

    public void setIntentFilters(List<IntentFilter> intentFilters) {
        this.intentFilters = intentFilters;
    }

    public List<MetaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<MetaData> metaData) {
        this.metaData = metaData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;

        Activity activity = (Activity) o;

        if (!name.equals(activity.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String getIdentifier() {
        return name;
    }
}
