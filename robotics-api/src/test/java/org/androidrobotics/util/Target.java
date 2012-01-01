package org.androidrobotics.util;

public class Target extends TargetSuper {
    private String value;

    public Target() {
        super();
        //noop
    }

    private Target(String value) {
        this.value = value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}