package org.androidtransfuse.util;

public class Target extends TargetSuper {
    private String value;

    public Target() {
        super();
        //noop
    }

    private Target(String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private void setPrivateValue(String value) {
        this.value = value;
    }

    private String getPrivateValue() {
        return value;
    }

    public String getValue() {
        return value;
    }
}