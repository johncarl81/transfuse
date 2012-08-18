package org.androidtransfuse.annotations;

public enum UIOptions implements LabeledEnum {
    NONE("none"),
    SPLIT_ACTION_BAR_WHEN_NARROW("splitActionBarWhenNarrow");

    private final String label;

    private UIOptions(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}