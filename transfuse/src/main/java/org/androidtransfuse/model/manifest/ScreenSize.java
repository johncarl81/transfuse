package org.androidtransfuse.model.manifest;

public enum ScreenSize implements LabeledEnum {
    SMALL("small"),
    NORMAL("normal"),
    LARGE("large"),
    XLARGE("xlarge");

    private String label;

    private ScreenSize(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}