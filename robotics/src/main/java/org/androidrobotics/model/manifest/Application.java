package org.androidrobotics.model.manifest;

/**
 * @author John Ericksen
 */
public class Application {

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

    /*
   can contain:
   <activity>
   <activity-alias>
   <service>
   <receiver>
   <provider>
   <uses-library>
    */

    /*
    android:allowTaskReparenting=["true" | "false"]
             android:backupAgent="string"
             android:debuggable=["true" | "false"]
             android:description="string resource"
             android:enabled=["true" | "false"]
             android:hasCode=["true" | "false"]
             android:hardwareAccelerated=["true" | "false"]
             android:icon="drawable resource"
             android:killAfterRestore=["true" | "false"]
             android:label="string resource"
             android:logo="drawable resource"
             android:manageSpaceActivity="string"
             android:name="string"
             android:permission="string"
             android:persistent=["true" | "false"]
             android:process="string"
             android:restoreAnyVersion=["true" | "false"]
             android:taskAffinity="string"
             android:theme="resource or theme"
             android:uiOptions=["none" | "splitActionBarWhenNarrow"]
     */
}
