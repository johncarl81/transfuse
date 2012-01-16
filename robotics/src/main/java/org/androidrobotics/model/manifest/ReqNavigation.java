package org.androidrobotics.model.manifest;

public enum ReqNavigation implements LabeledEnum {
    UNDEFINED("undefined"),
    NONAV("nonav"),
    DPAD("dpad"),
    TRACKBALL("trackball"),
    WHEEL("wheel");

    private String label;

    private ReqNavigation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}