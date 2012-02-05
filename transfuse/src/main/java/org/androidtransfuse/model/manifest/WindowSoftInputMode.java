package org.androidtransfuse.model.manifest;

public enum WindowSoftInputMode implements LabeledEnum {
    STATEUNSPECIFIED("stateUnspecified"),
    STATEUNCHANGED("stateUnchanged"),
    STATEHIDDEN("stateHidden"),
    STATEALWAYSHIDDEN("stateAlwaysHidden"),
    STATEVISIBLE("stateVisible"),
    STATEALWAYSVISIBLE("stateAlwaysVisible"),
    ADJUSTUNSPECIFIED("adjustUnspecified"),
    ADJUSTRESIZE("adjustResize"),
    ADJUSTPAN("adjustPan");

    private String label;

    private WindowSoftInputMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}