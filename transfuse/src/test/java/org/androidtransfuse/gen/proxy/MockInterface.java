package org.androidtransfuse.gen.proxy;

public interface MockInterface {
    void execute();

    String getValue();

    void setValue(String value);

    String passThroughValue(String input);
}