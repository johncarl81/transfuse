package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.androidrobotics.processor.MergeCollection;
import org.androidrobotics.processor.Mergeable;

import java.util.ArrayList;
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
public class IntentFilter extends Mergeable<String> {

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
    @MergeCollection(targetType = ArrayList.class)
    private List<Action> actions = new ArrayList<Action>();
    @XStreamImplicit(itemFieldName = "category")
    @MergeCollection(targetType = ArrayList.class)
    private List<Category> categories = new ArrayList<Category>();
    @XStreamImplicit(itemFieldName = "data")
    private List<Data> data = new ArrayList<Data>();

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

    @Override
    public String getIdentifier() {
        return "IntentFilter";
    }
}
