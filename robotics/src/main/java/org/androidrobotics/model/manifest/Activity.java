package org.androidrobotics.model.manifest;

/**
 * @author John Ericksen
 */
public class Activity {

    private Boolean allowTaskReparenting;
    private Boolean alwaysRetainTaskState;
    private Boolean clearTaskOnLaunch;
    private ConfigChanges configChanges;
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

    /*
   can contain:
<intent-filter>
<meta-data>
    */

    /*
    android:allowTaskReparenting=["true" | "false"]
          android:alwaysRetainTaskState=["true" | "false"]
          android:clearTaskOnLaunch=["true" | "false"]
          android:configChanges=["mcc", "mnc", "locale",
                                 "touchscreen", "keyboard", "keyboardHidden",
                                 "navigation", "screenLayout", "fontScale", "uiMode",
                                 "orientation", "screenSize", "smallestScreenSize"]
          android:enabled=["true" | "false"]
          android:excludeFromRecents=["true" | "false"]
          android:exported=["true" | "false"]
          android:finishOnTaskLaunch=["true" | "false"]
          android:hardwareAccelerated=["true" | "false"]
          android:icon="drawable resource"
          android:label="string resource"
          android:launchMode=["multiple" | "singleTop" |
                              "singleTask" | "singleInstance"]
          android:multiprocess=["true" | "false"]
          android:name="string"
          android:noHistory=["true" | "false"]
          android:permission="string"
          android:process="string"
          android:screenOrientation=["unspecified" | "user" | "behind" |
                                     "landscape" | "portrait" |
                                     "reverseLandscape" | "reversePortrait" |
                                     "sensorLandscape" | "sensorPortrait" |
                                     "sensor" | "fullSensor" | "nosensor"]
          android:stateNotNeeded=["true" | "false"]
          android:taskAffinity="string"
          android:theme="resource or theme"
          android:uiOptions=["none" | "splitActionBarWhenNarrow"]
          android:windowSoftInputMode=["stateUnspecified",
                                       "stateUnchanged", "stateHidden",
                                       "stateAlwaysHidden", "stateVisible",
                                       "stateAlwaysVisible", "adjustUnspecified",
                                       "adjustResize", "adjustPan"] >
     */


}
