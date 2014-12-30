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

import javax.xml.bind.annotation.XmlAttribute;


/**
 * attributes:
 * android:resizeable=["true"| "false"]
 * android:smallScreens=["true" | "false"]
 * android:normalScreens=["true" | "false"]
 * android:largeScreens=["true" | "false"]
 * android:xlargeScreens=["true" | "false"]
 * android:anyDensity=["true" | "false"]
 * android:requiresSmallestWidthDp="integer"
 * android:compatibleWidthLimitDp="integer"
 * android:largestWidthLimitDp="integer"
 *
 * @author John Ericksen
 */
public class SupportsScreens extends ManifestBase {

    private Boolean resizeable;
    private Boolean smallScreen;
    private Boolean normalScreens;
    private Boolean largeScreens;
    private Boolean xlargeScreens;
    private Boolean anyDensity;
    private String requiresSmallestWidthDp;
    private String compatibleWidthLimitDp;
    private String largestWidthLimitDp;

    @XmlAttribute(name = "resizeable", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getResizeable() {
        return resizeable;
    }

    public void setResizeable(Boolean resizeable) {
        this.resizeable = resizeable;
    }

    @XmlAttribute(name = "smallScreens", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getSmallScreen() {
        return smallScreen;
    }

    public void setSmallScreen(Boolean smallScreen) {
        this.smallScreen = smallScreen;
    }

    @XmlAttribute(name = "normalScreens", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getNormalScreens() {
        return normalScreens;
    }

    public void setNormalScreens(Boolean normalScreens) {
        this.normalScreens = normalScreens;
    }

    @XmlAttribute(name = "largeScreens", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getLargeScreens() {
        return largeScreens;
    }

    public void setLargeScreens(Boolean largeScreens) {
        this.largeScreens = largeScreens;
    }

    @XmlAttribute(name = "xlargeScreens", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getXlargeScreens() {
        return xlargeScreens;
    }

    public void setXlargeScreens(Boolean xlargeScreens) {
        this.xlargeScreens = xlargeScreens;
    }

    @XmlAttribute(name = "anyDensity", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getAnyDensity() {
        return anyDensity;
    }

    public void setAnyDensity(Boolean anyDensity) {
        this.anyDensity = anyDensity;
    }

    @XmlAttribute(name = "requiresSmallestWidthDp", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getRequiresSmallestWidthDp() {
        return requiresSmallestWidthDp;
    }

    public void setRequiresSmallestWidthDp(String requiresSmallestWidthDp) {
        this.requiresSmallestWidthDp = requiresSmallestWidthDp;
    }

    @XmlAttribute(name = "compatibleWidthLimitDp", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getCompatibleWidthLimitDp() {
        return compatibleWidthLimitDp;
    }

    public void setCompatibleWidthLimitDp(String compatibleWidthLimitDp) {
        this.compatibleWidthLimitDp = compatibleWidthLimitDp;
    }

    @XmlAttribute(name = "largestWidthLimitDp", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getLargestWidthLimitDp() {
        return largestWidthLimitDp;
    }

    public void setLargestWidthLimitDp(String largestWidthLimitDp) {
        this.largestWidthLimitDp = largestWidthLimitDp;
    }
}
