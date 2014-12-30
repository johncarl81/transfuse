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

import org.androidtransfuse.model.Identified;
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.Merge;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * attributes:
 * android:name="string"
 * android:required=["true" | "false"]
 * android:glEsVersion="integer"
 *
 * @author John Ericksen
 */
public class UsesFeature extends Mergeable implements Identified {

    private String name;
    private Boolean required;
    private Integer glEsVersion;

    @Merge("n")
    @XmlAttribute(name = "name", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Merge("r")
    @XmlAttribute(name = "required", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    @Merge("g")
    @XmlAttribute(name = "glEsVersion", namespace = ManifestNamespaceMapper.ANDROID_URI)
    @XmlJavaTypeAdapter(HexadecimalIntegerConverter.class)
    public Integer getGlEsVersion() {
        return glEsVersion;
    }

    public void setGlEsVersion(Integer glEsVersion) {
        this.glEsVersion = glEsVersion;
    }

    @XmlTransient
    public String getIdentifier(){
        return name + glEsVersion;
    }
    
}
