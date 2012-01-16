package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * attributes:
 * android:icon="drawable resource"
 * android:label="string resource"
 * android:priority="integer"
 * <p/>
 * must contain:
 * <action>
 * <p/>
 * can contain:
 * <category>
 * <data>
 *
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
