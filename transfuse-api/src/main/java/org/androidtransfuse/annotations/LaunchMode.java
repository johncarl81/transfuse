package org.androidtransfuse.annotations;

public enum LaunchMode implements LabeledEnum {
    STANDARD("standard"),
    SINGLETOP("singleTop"),
    SINGLETASK("singleTask"),
    SINGLEINSTANCE("singleInstance");

    private String label;

    private LaunchMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}