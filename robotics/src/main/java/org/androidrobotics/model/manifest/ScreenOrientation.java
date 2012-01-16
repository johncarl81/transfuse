package org.androidrobotics.model.manifest;

public enum ScreenOrientation implements LabeledEnum {
    UNSPECIFIED("unspecified"),
    USER("user"),
    BEHIND("behind"),
    LANDSCAPE("landscape"),
    PORTRAIT("portrait"),
    REVERSELANDSCAPE("reverseLandscape"),
    REVERSEPORTRAIT("reversePortrait"),
    SENSORLANDSCAPE("sensorLandscape"),
    SENSORPORTRAIT("sensorPortrait"),
    SENSOR("sensor"),
    FULLSENSOR("fullSensor"),
    NOSENSOR("nosensor");

    private String label;

    private ScreenOrientation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}