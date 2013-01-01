/**
 * Copyright 2013 John Ericksen
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
public class IntentFilter extends Mergeable {

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
    private List<Action> actions = new ArrayList<Action>();
    @XStreamImplicit(itemFieldName = "category")
    private List<Category> categories = new ArrayList<Category>();
    @XStreamImplicit(itemFieldName = "data")
    private List<Data> data = new ArrayList<Data>();

    @Merge("i")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Merge("l")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Merge("p")
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @MergeCollection(collectionType = ArrayList.class, type = Action.class)
    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @MergeCollection(collectionType = ArrayList.class, type = Category.class)
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
}
