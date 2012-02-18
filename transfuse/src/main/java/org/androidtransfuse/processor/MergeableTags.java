package org.androidtransfuse.processor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class MergeableTags {
    
    private Set<String> tags;

    public MergeableTags(){
        tags = new HashSet<String>();
    }

    public MergeableTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<String> getTags() {
        return tags;
    }
}
