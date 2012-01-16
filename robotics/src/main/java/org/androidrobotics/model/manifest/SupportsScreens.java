package org.androidrobotics.model.manifest;

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
    @XStreamAlias("android:smallScreen")
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
    private Integer requiresSmallestWidthDp;
    @XStreamAlias("android:compatibleWidthLimitDp")
    @XStreamAsAttribute
    private Integer compatibleWidthLimitDp;
    @XStreamAlias("android:largestWidthLimitDp")
    @XStreamAsAttribute
    private Integer largestWidthLimitDp;

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

    public Integer getRequiresSmallestWidthDp() {
        return requiresSmallestWidthDp;
    }

    public void setRequiresSmallestWidthDp(Integer requiresSmallestWidthDp) {
        this.requiresSmallestWidthDp = requiresSmallestWidthDp;
    }

    public Integer getCompatibleWidthLimitDp() {
        return compatibleWidthLimitDp;
    }

    public void setCompatibleWidthLimitDp(Integer compatibleWidthLimitDp) {
        this.compatibleWidthLimitDp = compatibleWidthLimitDp;
    }

    public Integer getLargestWidthLimitDp() {
        return largestWidthLimitDp;
    }

    public void setLargestWidthLimitDp(Integer largestWidthLimitDp) {
        this.largestWidthLimitDp = largestWidthLimitDp;
    }
}
