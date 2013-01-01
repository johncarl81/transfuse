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
public class SupportsScreens {

    @XStreamAlias("android:resizeable")
    @XStreamAsAttribute
    private Boolean resizeable;
    @XStreamAlias("android:smallScreens")
    @XStreamAsAttribute
    private Boolean smallScreen;
    @XStreamAlias("android:normalScreens")
    @XStreamAsAttribute
    private Boolean normalScreens;
    @XStreamAlias("android:largeScreens")
    @XStreamAsAttribute
    private Boolean largeScreens;
    @XStreamAlias("android:xlargeScreens")
    @XStreamAsAttribute
    private Boolean xlargeScreens;
    @XStreamAlias("android:anyDensity")
    @XStreamAsAttribute
    private Boolean anyDensity;
    @XStreamAlias("android:requiresSmallestWidthDp")
    @XStreamAsAttribute
    private String requiresSmallestWidthDp;
    @XStreamAlias("android:compatibleWidthLimitDp")
    @XStreamAsAttribute
    private String compatibleWidthLimitDp;
    @XStreamAlias("android:largestWidthLimitDp")
    @XStreamAsAttribute
    private String largestWidthLimitDp;

    public Boolean getResizeable() {
        return resizeable;
    }

    public void setResizeable(Boolean resizeable) {
        this.resizeable = resizeable;
    }

    public Boolean getSmallScreen() {
        return smallScreen;
    }

    public void setSmallScreen(Boolean smallScreen) {
        this.smallScreen = smallScreen;
    }

    public Boolean getNormalScreens() {
        return normalScreens;
    }

    public void setNormalScreens(Boolean normalScreens) {
        this.normalScreens = normalScreens;
    }

    public Boolean getLargeScreens() {
        return largeScreens;
    }

    public void setLargeScreens(Boolean largeScreens) {
        this.largeScreens = largeScreens;
    }

    public Boolean getXlargeScreens() {
        return xlargeScreens;
    }

    public void setXlargeScreens(Boolean xlargeScreens) {
        this.xlargeScreens = xlargeScreens;
    }

    public Boolean getAnyDensity() {
        return anyDensity;
    }

    public void setAnyDensity(Boolean anyDensity) {
        this.anyDensity = anyDensity;
    }

    public String getRequiresSmallestWidthDp() {
        return requiresSmallestWidthDp;
    }

    public void setRequiresSmallestWidthDp(String requiresSmallestWidthDp) {
        this.requiresSmallestWidthDp = requiresSmallestWidthDp;
    }

    public String getCompatibleWidthLimitDp() {
        return compatibleWidthLimitDp;
    }

    public void setCompatibleWidthLimitDp(String compatibleWidthLimitDp) {
        this.compatibleWidthLimitDp = compatibleWidthLimitDp;
    }

    public String getLargestWidthLimitDp() {
        return largestWidthLimitDp;
    }

    public void setLargestWidthLimitDp(String largestWidthLimitDp) {
        this.largestWidthLimitDp = largestWidthLimitDp;
    }
}
