package org.androidrobotics.model.manifest;

/**
 * @author John Ericksen
 */
public class SupportsScreens {

    private Boolean resizeable;
    private Boolean smallScreen;
    private Boolean normalScreens;
    private Boolean largeScreens;
    private Boolean xlargeScreens;
    private Boolean anyDensity;
    private Integer requiresSmallestWidthDp;
    private Integer compatibleWidthLimitDp;
    private Integer largestWidthLimitDp;

    /*
    androidresizeable=["true"| "false"]
                  android:smallScreens=["true" | "false"]
                  android:normalScreens=["true" | "false"]
                  android:largeScreens=["true" | "false"]
                  android:xlargeScreens=["true" | "false"]
                  android:anyDensity=["true" | "false"]
                  android:requiresSmallestWidthDp="integer"
                  android:compatibleWidthLimitDp="integer"
                  android:largestWidthLimitDp="integer"
     */
}
