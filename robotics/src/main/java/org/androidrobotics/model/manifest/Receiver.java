package org.androidrobotics.model.manifest;

import java.util.List;

/**
 * @author John Ericksen
 */
public class Receiver {

    private Boolean enabled;
    private Boolean exported;
    private String icon;
    private String label;
    private String name;
    private String permission;
    private String process;

    private List<IntentFilter> intentFilters;
    private List<MetaData> metaData;

    /*
    android:enabled=["true" | "false"]
          android:exported=["true" | "false"]
          android:icon="drawable resource"
          android:label="string resource"
          android:name="string"
          android:permission="string"
          android:process="string"
     */

    /*
    can contain:
<intent-filter>
<meta-data>
     */
}
