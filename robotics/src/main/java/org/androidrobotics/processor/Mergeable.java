package org.androidrobotics.processor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author John Ericksen
 */
public abstract class Mergeable<T> {

    @XStreamAlias("transfuse")
    @XStreamAsAttribute
    private String tag;

    protected Mergeable() {
    }

    protected Mergeable(String tag) {
        this.tag = tag;
    }

    public abstract T getIdentifier();

    public void setMergeTag(String tag) {
        this.tag = tag;
    }

    public String getMergeTag() {
        return tag;
    }

}
