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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * attributes:
 * android:screenSize=["small" | "normal" | "large" | "xlarge"]
 * android:screenDensity=["ldpi" | "mdpi" | "hdpi" | "xhdpi"]
 *
 * @author John Ericksen
 */
public class Screen {

    private ScreenSize screenSize;
    private ScreenDensity screenDensity;

    @XmlAttribute(name = "screenSize", namespace = ManifestNamespaceMapper.ANDROID_URI)
    @XmlJavaTypeAdapter(LabeledConverter.ScreenSizeConverter.class)
    public ScreenSize getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(ScreenSize screenSize) {
        this.screenSize = screenSize;
    }

    @XmlAttribute(name = "screenDensity", namespace = ManifestNamespaceMapper.ANDROID_URI)
    @XmlJavaTypeAdapter(LabeledConverter.ScreenDensityConverter.class)
    public ScreenDensity getScreenDensity() {
        return screenDensity;
    }

    public void setScreenDensity(ScreenDensity screenDensity) {
        this.screenDensity = screenDensity;
    }
}
