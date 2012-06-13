package org.androidtransfuse.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.Merge;
import org.androidtransfuse.processor.MergeCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * attributes:
 * android:enabled=["true" | "false"]
 * android:exported=["true" | "false"]
 * android:icon="drawable resource"
 * android:label="string resource"
 * android:name="string"
 * android:permission="string"
 * android:process="string"
 * <p/>
 * can contain:
 * <meta-data>
 * <intent-filter>
 *
 * @author John Ericksen
 */
public class Receiver extends Mergeable implements Comparable<Receiver>{

    @XStreamAlias("android:enabled")
    @XStreamAsAttribute
    private Boolean enabled;
    @XStreamAlias("android:exported")
    @XStreamAsAttribute
    private Boolean exported;
    @XStreamAlias("android:icon")
    @XStreamAsAttribute
    private String icon;
    @XStreamAlias("android:label")
    @XStreamAsAttribute
    private String label;
    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("android:permission")
    @XStreamAsAttribute
    private String permission;
    @XStreamAlias("android:process")
    @XStreamAsAttribute
    private String process;

    @XStreamImplicit(itemFieldName = "intent-filter")
    private List<IntentFilter> intentFilters = new ArrayList<IntentFilter>();
    @XStreamImplicit(itemFieldName = "meta-data")
    private List<MetaData> metaData = new ArrayList<MetaData>();

    @Merge(value = "e")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Merge(value = "x")
    public Boolean getExported() {
        return exported;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
    }

    @Merge(value = "i")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Merge(value = "l")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Merge(value = "n")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Merge(value = "p")
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Merge(value = "r")
    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    @MergeCollection(collectionType = ArrayList.class, type = IntentFilter.class)
    public List<IntentFilter> getIntentFilters() {
        return intentFilters;
    }

    public void setIntentFilters(List<IntentFilter> intentFilters) {
        this.intentFilters = intentFilters;
    }

    @MergeCollection(collectionType = ArrayList.class, type = MetaData.class)
    public List<MetaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<MetaData> metaData) {
        this.metaData = metaData;
    }

    @Override
    public int compareTo(Receiver receiver) {
        return getName().compareTo(receiver.getName());
    }

    @Override
    public String getIdentifier() {
        return name;
    }
}
