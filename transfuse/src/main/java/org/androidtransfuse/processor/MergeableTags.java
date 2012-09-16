package org.androidtransfuse.processor;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author John Ericksen
 */
public class MergeableTags {

    //treeset used for tag order
    private final Set<String> tags = new TreeSet<String>();

    public MergeableTags() {
        //empty bean constructor
    }

    public MergeableTags(Set<String> tags) {
        this.tags.addAll(tags);
    }

    public Set<String> getTags() {
        return tags;
    }
}
