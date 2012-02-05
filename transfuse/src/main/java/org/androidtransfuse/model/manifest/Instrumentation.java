package org.androidtransfuse.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * attributes:
 * android:functionalTest=["true" | "false"]
 * android:handleProfiling=["true" | "false"]
 * android:icon="drawable resource"
 * android:label="string resource"
 * android:name="string"
 * android:targetPackage="string"
 *
 * @author John Ericksen
 */
public class Instrumentation {

    @XStreamAlias("android:icon")
    @XStreamAsAttribute
    private String icon;
    @XStreamAlias("android:label")
    @XStreamAsAttribute
    private String label;
    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("android:functionalTest")
    @XStreamAsAttribute
    private Boolean functionalTest;
    @XStreamAlias("android:handleProfiling")
    @XStreamAsAttribute
    private Boolean handleProfiling;
    @XStreamAlias("android:targetPackage")
    @XStreamAsAttribute
    private String targetPackage;

    public Boolean getFunctionalTest() {
        return functionalTest;
    }

    public void setFunctionalTest(Boolean functionalTest) {
        this.functionalTest = functionalTest;
    }

    public Boolean getHandleProfiling() {
        return handleProfiling;
    }

    public void setHandleProfiling(Boolean handleProfiling) {
        this.handleProfiling = handleProfiling;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }
}
