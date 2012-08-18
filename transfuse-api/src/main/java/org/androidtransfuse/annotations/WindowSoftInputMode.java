package org.androidtransfuse.annotations;

public enum WindowSoftInputMode implements LabeledEnum {
    STATE_UNSPECIFIED("stateUnspecified"),
    STATE_UNCHANGED("stateUnchanged"),
    STATE_HIDDEN("stateHidden"),
    STATE_ALWAYS_HIDDEN("stateAlwaysHidden"),
    STATE_VISIBLE("stateVisible"),
    STATE_ALWAYS_VISIBLE("stateAlwaysVisible"),
    ADJUST_UNSPECIFIED("adjustUnspecified"),
    ADJUST_RESIZE("adjustResize"),
    ADJUST_PAN("adjustPan");

    private final String label;

    private WindowSoftInputMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}