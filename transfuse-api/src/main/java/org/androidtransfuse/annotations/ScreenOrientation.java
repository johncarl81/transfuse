package org.androidtransfuse.annotations;

public enum ScreenOrientation implements LabeledEnum {
    UNSPECIFIED("unspecified"),
    USER("user"),
    BEHIND("behind"),
    LANDSCAPE("landscape"),
    PORTRAIT("portrait"),
    REVERSE_LANDSCAPE("reverseLandscape"),
    REVERSE_PORTRAIT("reversePortrait"),
    SENSOR_LANDSCAPE("sensorLandscape"),
    SENSOR_PORTRAIT("sensorPortrait"),
    SENSOR("sensor"),
    FULL_SENSOR("fullSensor"),
    NO_SENSOR("nosensor");

    private String label;

    private ScreenOrientation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}