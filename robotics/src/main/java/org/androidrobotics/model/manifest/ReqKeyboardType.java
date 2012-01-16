package org.androidrobotics.model.manifest;

public enum ReqKeyboardType implements LabeledEnum {
    UNDEFINED("undefined"),
    NOKEYS("nokeys"),
    QUERTY("qwerty"),
    TWELVEKEY("twelvekey");

    private String label;

    private ReqKeyboardType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}