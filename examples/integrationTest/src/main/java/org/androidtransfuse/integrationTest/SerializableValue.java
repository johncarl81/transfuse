package org.androidtransfuse.integrationTest;

import java.io.Serializable;

/**
 * @author John Ericksen
 */
public class SerializableValue implements Serializable {

    private static final long serialVersionUID = 6426768294623109510L;
    private String value;

    public SerializableValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
