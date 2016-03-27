package example.test;

import java.util.Map;

public class TestClass extends Base implements Test {

    private String value;
    private Values[] valueArray;

    private enum Values {
        A, B, C
    }

    public TestClass() {
    }

    public TestClass(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}