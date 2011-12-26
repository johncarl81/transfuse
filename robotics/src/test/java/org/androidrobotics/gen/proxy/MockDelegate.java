package org.androidrobotics.gen.proxy;

import org.androidrobotics.gen.VirtualProxyGeneratorTest;

public class MockDelegate implements MockInterface {

    private boolean exectuted = false;
    private String valueOne;
    private String passThroughValue;

    @Override
    public void execute() {
        exectuted = true;
    }

    @Override
    public String getValue() {
        return VirtualProxyGeneratorTest.TEST_VALUE;
    }

    @Override
    public void setValue(String value) {
        valueOne = value;
    }

    @Override
    public String passThroughValue(String input) {
        passThroughValue = input;
        return VirtualProxyGeneratorTest.TEST_VALUE;
    }

    public boolean validate(String inputOne, String inputTwo) {
        return exectuted && inputOne.equals(valueOne) && inputTwo.equals(passThroughValue);
    }
}