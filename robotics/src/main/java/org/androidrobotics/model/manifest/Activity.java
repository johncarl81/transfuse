package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

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
public class Activity {

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
    private List<IntentFilter> intentFilters;
    @XStreamImplicit(itemFieldName = "meta-data")
    private List<MetaData> metaData;

}
