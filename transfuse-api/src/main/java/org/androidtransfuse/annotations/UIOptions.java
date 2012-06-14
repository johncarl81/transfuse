package org.androidtransfuse.annotations;

public enum UIOptions implements LabeledEnum {
    NONE("none"),
    SPLITACTIONBARWHENNARROW("splitActionBarWhenNarrow");

    private String label;

    private UIOptions(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}