package org.androidtransfuse.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.androidtransfuse.annotations.LaunchMode;
import org.androidtransfuse.annotations.ScreenOrientation;
import org.androidtransfuse.annotations.UIOptions;
import org.androidtransfuse.annotations.WindowSoftInputMode;
import org.androidtransfuse.model.Identified;
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.Merge;
import org.androidtransfuse.processor.MergeCollection;
import org.apache.commons.lang.builder.EqualsBuilder;

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
public class Activity extends Mergeable implements Comparable<Activity>, Identified {

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
    private String configChanges;
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
    private List<IntentFilter> intentFilters = new ArrayList<IntentFilter>();
    @XStreamImplicit(itemFieldName = "meta-data")
    private List<MetaData> metaData = new ArrayList<MetaData>();

    @Merge("t")
    public Boolean getAllowTaskReparenting() {
        return allowTaskReparenting;
    }

    public void setAllowTaskReparenting(Boolean allowTaskReparenting) {
        this.allowTaskReparenting = allowTaskReparenting;
    }

    @Merge("s")
    public Boolean getAlwaysRetainTaskState() {
        return alwaysRetainTaskState;
    }

    public void setAlwaysRetainTaskState(Boolean alwaysRetainTaskState) {
        this.alwaysRetainTaskState = alwaysRetainTaskState;
    }

    @Merge("c")
    public Boolean getClearTaskOnLaunch() {
        return clearTaskOnLaunch;
    }

    public void setClearTaskOnLaunch(Boolean clearTaskOnLaunch) {
        this.clearTaskOnLaunch = clearTaskOnLaunch;
    }

    @Merge("a")
    public String getConfigChanges() {
        return configChanges;
    }

    public void setConfigChanges(String configChanges) {
        this.configChanges = configChanges;
    }

    @Merge("e")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Merge("r")
    public Boolean getExcludeFromRecents() {
        return excludeFromRecents;
    }

    public void setExcludeFromRecents(Boolean excludeFromRecents) {
        this.excludeFromRecents = excludeFromRecents;
    }

    @Merge("x")
    public Boolean getExported() {
        return exported;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
    }

    @Merge("f")
    public Boolean getFinishOnTaskLaunch() {
        return finishOnTaskLaunch;
    }

    public void setFinishOnTaskLaunch(Boolean finishOnTaskLaunch) {
        this.finishOnTaskLaunch = finishOnTaskLaunch;
    }

    @Merge("d")
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

    @Merge("l")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Merge("u")
    public LaunchMode getLaunchMode() {
        return launchMode;
    }

    public void setLaunchMode(LaunchMode launchMode) {
        this.launchMode = launchMode;
    }

    @Merge("m")
    public Boolean getMultiprocess() {
        return multiprocess;
    }

    public void setMultiprocess(Boolean multiprocess) {
        this.multiprocess = multiprocess;
    }

    @Merge("n")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Merge("y")
    public Boolean getNoHistory() {
        return noHistory;
    }

    public void setNoHistory(Boolean noHistory) {
        this.noHistory = noHistory;
    }

    @Merge("p")
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Merge("o")
    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    @Merge("g")
    public ScreenOrientation getScreenOrientation() {
        return screenOrientation;
    }

    public void setScreenOrientation(ScreenOrientation screenOrientation) {
        this.screenOrientation = screenOrientation;
    }

    @Merge("d")
    public Boolean getStateNotNeeded() {
        return stateNotNeeded;
    }

    public void setStateNotNeeded(Boolean stateNotNeeded) {
        this.stateNotNeeded = stateNotNeeded;
    }

    @Merge("y")
    public String getTaskAffinity() {
        return taskAffinity;
    }

    public void setTaskAffinity(String taskAffinity) {
        this.taskAffinity = taskAffinity;
    }

    @Merge("h")
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @Merge("b")
    public UIOptions getUiOptions() {
        return uiOptions;
    }

    public void setUiOptions(UIOptions uiOptions) {
        this.uiOptions = uiOptions;
    }

    @Merge("w")
    public WindowSoftInputMode getWindowSoftInputMode() {
        return windowSoftInputMode;
    }

    public void setWindowSoftInputMode(WindowSoftInputMode windowSoftInputMode) {
        this.windowSoftInputMode = windowSoftInputMode;
    }

    @MergeCollection(collectionType = ArrayList.class, type = IntentFilter.class)
    public List<IntentFilter> getIntentFilters() {
        return intentFilters;
    }

    public void setIntentFilters(List<IntentFilter> intentFilters) {
        this.intentFilters = intentFilters;
    }

    @MergeCollection(collectionType = ArrayList.class, type = MetaData.class)
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
