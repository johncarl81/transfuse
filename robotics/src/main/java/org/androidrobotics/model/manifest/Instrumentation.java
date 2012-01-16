package org.androidrobotics.model.manifest;

/**
 * @author John Ericksen
 */
public class Instrumentation {

    private Boolean functionalTest;
    private Boolean handleProfiling;
    private String icon;
    private String label;
    private String name;
    private String targetPackage;

    /*
    android:functionalTest=["true" | "false"]
                 android:handleProfiling=["true" | "false"]
                 android:icon="drawable resource"
                 android:label="string resource"
                 android:name="string"
                 android:targetPackage="string"
     */
}
