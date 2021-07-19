/**
 * Copyright 2011-2015 John Ericksen
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

import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.Merge;
import org.androidtransfuse.processor.MergeCollection;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * attributes:
 * android:icon="drawable resource"
 * android:label="string resource"
 * android:priority="integer"
 *
 * must contain:
 * <action>
 *
 * can contain:
 * <category>
 * <data>
 *
 * @author John Ericksen
 */
public class IntentFilter extends Mergeable {

    private String icon;
    private String label;
    private Integer priority;
    private List<Action> actions = new ArrayList<Action>();
    private List<Category> categories = new ArrayList<Category>();
    private List<Data> data = new ArrayList<Data>();

    @Merge("i")
    @XmlAttribute(name = "icon", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Merge("l")
    @XmlAttribute(name = "label", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Merge("p")
    @XmlAttribute(name = "priority", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @MergeCollection(collectionType = ArrayList.class, type = Action.class)
    @XmlElement(name = "action")
    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @MergeCollection(collectionType = ArrayList.class, type = Category.class)
    @XmlElement(name = "category")
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @MergeCollection(collectionType = ArrayList.class, type = Data.class)
    @XmlElement(name = "data")
    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
