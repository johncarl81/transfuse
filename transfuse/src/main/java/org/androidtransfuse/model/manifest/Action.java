package org.androidtransfuse.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.androidtransfuse.model.Identified;
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.Merge;

/**
 * attributes:
 * android:name
 *
 * @author John Ericksen
 */
public class Action extends Mergeable implements Identified {

    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;

    public String getName() {
        return name;
    }

    @Merge("n")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIdentifier() {
        return name;
    }
}
