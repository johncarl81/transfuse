package org.androidtransfuse.model.manifest;

import org.androidtransfuse.annotations.LabeledEnum;

public enum InstallLocation implements LabeledEnum {
    AUTO("auto"),
    INTERNAL_ONLY("internalOnly"),
    PREFER_EXTERNAL("preferExternal");

    private String label;

    private InstallLocation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}