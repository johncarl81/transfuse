package org.androidrobotics.model.manifest;

import java.util.List;

/**
 * @author John Ericksen
 */
public class IntentFilter {

    private String icon;
    private String label;
    private Integer priority;

    private List<Action> actions;
    private List<Category> categories;
    private List<Data> data;
    /*
    must contain:
    <action>
    can contain:
    <category>
    <data>
    */

    /*
    android:icon="drawable resource"
               android:label="string resource"
               android:priority="integer"
     */
}
