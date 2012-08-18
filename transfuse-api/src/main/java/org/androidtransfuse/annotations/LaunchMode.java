package org.androidtransfuse.annotations;

public enum LaunchMode implements LabeledEnum {
    STANDARD("standard"),
    SINGLE_TOP("singleTop"),
    SINGLE_TASK("singleTask"),
    SINGLE_INSTANCE("singleInstance");

    private final String label;

    private LaunchMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}