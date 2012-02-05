package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.androidrobotics.processor.Mergeable;

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

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIdentifier() {
        return name;
    }
}
