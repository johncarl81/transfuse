package org.androidtransfuse.model.manifest;

public enum LaunchMode implements LabeledEnum {
    MULTIPLE("multiple"),
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