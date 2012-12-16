/**
 * Copyright 2012 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
