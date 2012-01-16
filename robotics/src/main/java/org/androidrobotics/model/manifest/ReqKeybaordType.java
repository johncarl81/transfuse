package org.androidrobotics.model.manifest;

public enum ReqKeybaordType implements LabeledEnum {
    UNDEFINED("undefined"),
    NOKEYS("nokeys"),
    QUERTY("qwerty"),
    TWELVEKEY("twelvekey");

    private String label;

    private ReqKeybaordType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}