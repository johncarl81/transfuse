package org.androidrobotics.model.manifest;

import java.util.List;

/**
 * @author John Ericksen
 */
public class Provider {

    private String authorities;
    private Boolean enabled;
    private Boolean exported;
    private Boolean grantUriPermissions;
    private String icon;
    private Integer initOrder;
    private String label;
    private Boolean multiprocess;
    private String name;
    private String permission;
    private String process;
    private String readPermission;
    private Boolean syncable;
    private String writePermission;

    private List<MetaData> metaData;
    private List<GrantUriPermission> grantUriPermissionList;
    private List<PathPermission> pathPermissions;

    /*
    can contain:
<meta-data>
<grant-uri-permission>
<path-permission>
     */

    /*
    android:authorities="list"
          android:enabled=["true" | "false"]
          android:exported=["true" | "false"]
          android:grantUriPermissions=["true" | "false"]
          android:icon="drawable resource"
          android:initOrder="integer"
          android:label="string resource"
          android:multiprocess=["true" | "false"]
          android:name="string"
          android:permission="string"
          android:process="string"
          android:readPermission="string"
          android:syncable=["true" | "false"]
          android:writePermission="string"
     */
}
