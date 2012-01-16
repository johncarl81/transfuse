package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * @author John Ericksen
 */
public class IntentFilter {

    @XStreamAlias("android:icon")
    @XStreamAsAttribute
    private String icon;
    @XStreamAlias("android:label")
    @XStreamAsAttribute
    private String label;
    @XStreamAlias("android:priority")
    @XStreamAsAttribute
    private Integer priority;
    @XStreamImplicit(itemFieldName = "action")
    private List<Action> actions;
    @XStreamImplicit(itemFieldName = "category")
    private List<Category> categories;
    @XStreamImplicit(itemFieldName = "data")
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
