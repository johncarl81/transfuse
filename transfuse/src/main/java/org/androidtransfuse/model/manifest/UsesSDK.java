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
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.Merge;

/**
 * attributes:
 * android:minSdkVersion="integer"
 * android:targetSdkVersion="integer"
 * android:maxSdkVersion="integer"
 *
 * @author John Ericksen
 */
public class UsesSDK extends Mergeable {

    @XStreamAlias("android:minSdkVersion")
    @XStreamAsAttribute
    private Integer minSdkVersion;
    @XStreamAlias("android:targetSdkVersion")
    @XStreamAsAttribute
    private Integer targetSdkVersion;
    @XStreamAlias("android:maxSdkVersion")
    @XStreamAsAttribute
    private Integer maxSdkVersion;

    @Merge("m")
    public Integer getMinSdkVersion() {
        return minSdkVersion;
    }

    public void setMinSdkVersion(Integer minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    @Merge("t")
    public Integer getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(Integer targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    @Merge("x")
    public Integer getMaxSdkVersion() {
        return maxSdkVersion;
    }

    public void setMaxSdkVersion(Integer maxSdkVersion) {
        this.maxSdkVersion = maxSdkVersion;
    }
}
