package org.androidtransfuse.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.androidtransfuse.processor.MergeableTags;

/**
 * @author John Ericksen
 */

public class Mergeable {

    private static final String GENERATED_TOKEN = "+";

    @XStreamAlias("t:tag")
    @XStreamAsAttribute
    private MergeableTags tags = new MergeableTags();

    public int getMergeTagSize(){
        if(tags != null){
            return tags.getTags().size();
        }
        return 0;
    }

    public void addMergeTag(String tag) {
        tags.getTags().add(tag);
    }

    public boolean containsTag(String tag) {
        return tags != null && tags.getTags().contains(tag);
    }

    public void removeMergeTag(String tag) {
        if(tags != null){
            tags.getTags().remove(tag);
        }
    }

    public boolean isGenerated(){
        return containsTag(GENERATED_TOKEN);
    }

    public synchronized void setGenerated(boolean generated) {
        if(tags == null){
            tags = new MergeableTags();
        }
        this.tags.getTags().add(GENERATED_TOKEN);
    }
}
