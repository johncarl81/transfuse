package org.androidrobotics.gen;

import org.androidrobotics.gen.target.A;
import org.androidrobotics.gen.target.FieldInjectable;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class VariableNamerTest {

    private VariableNamer variableNamer;

    @Before
    public void setup() {
        variableNamer = new VariableNamer();
    }

    @Test
    public void testBuildName() {
        assertEquals("fieldInjectable_0", variableNamer.generateName(FieldInjectable.class));
    }

    @Test
    public void testMultipleNames() {
        assertEquals("fieldInjectable_0", variableNamer.generateName(FieldInjectable.class));
        assertEquals("fieldInjectable_1", variableNamer.generateName(FieldInjectable.class));
        assertEquals("fieldInjectable_2", variableNamer.generateName(FieldInjectable.class));
    }

    @Test
    public void testSmallClassName() {
        assertEquals("a_0", variableNamer.generateName(A.class));
    }


}
