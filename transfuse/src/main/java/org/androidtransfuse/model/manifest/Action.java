package org.androidtransfuse.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import org.androidtransfuse.processor.Merge;
import org.androidtransfuse.processor.Mergeable;
import org.androidtransfuse.processor.MergeableTagConverter;
import org.androidtransfuse.processor.MergeableTags;

import java.util.Set;

/**
 * attributes:
 * android:name
 *
 * @author John Ericksen
 */
public class Action extends Mergeable<String> {

    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;

    public String getName() {
        return name;
    }

    @Merge(value = "n")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIdentifier() {
        return name;
    }
}
