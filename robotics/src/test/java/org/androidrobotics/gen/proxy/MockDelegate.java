package org.androidrobotics.gen.proxy;

import org.androidrobotics.gen.VirtualProxyGeneratorTest;

public class MockDelegate implements MockInterface, SecondMockInteface {

    private boolean exectuted = false;
    private String valueOne;
    private String passThroughValue;
    private int secondValue = 0;

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
    public void setValue(int value) {
        this.secondValue = value;
    }

    @Override
    public String passThroughValue(String input) {
        passThroughValue = input;
        return VirtualProxyGeneratorTest.TEST_VALUE;
    }

    public boolean primitiveCall() {
        return true;
    }

    public boolean validate(String inputOne, String inputTwo, int secondValue) {
        return exectuted && inputOne.equals(valueOne) && inputTwo.equals(passThroughValue) && this.secondValue == secondValue;
    }
}