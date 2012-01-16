package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

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
public class Application {

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
    private List<Activity> activities;
}
