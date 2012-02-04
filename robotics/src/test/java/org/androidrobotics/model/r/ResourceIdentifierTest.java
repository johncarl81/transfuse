package org.androidrobotics.model.r;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.util.JavaUtilLogger;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static junit.framework.Assert.*;

/**
 * @author John Ericksen
 */
public class ResourceIdentifierTest {

    @Inject
    private ASTClassFactory astClassFactory;
    private ASTType rInnerType;
    private ASTType otherInnerType;

    private static class RInnerType {
    }

    private static class OtherInnerType {
    }

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this)));
        injector.injectMembers(this);

        rInnerType = astClassFactory.buildASTClassType(RInnerType.class);
        otherInnerType = astClassFactory.buildASTClassType(OtherInnerType.class);
    }

    @Test
    public void testEquality() {
        ResourceIdentifier valueIdentifier = new ResourceIdentifier(rInnerType, "value");
        ResourceIdentifier valueIdentifierDuplicate = new ResourceIdentifier(rInnerType, "value");
        ResourceIdentifier secondValueIdentifier = new ResourceIdentifier(rInnerType, "secondValue");
        ResourceIdentifier otherValueIdentifier = new ResourceIdentifier(otherInnerType, "value");

        assertTrue(valueIdentifier.equals(valueIdentifierDuplicate));
        assertTrue(valueIdentifierDuplicate.equals(valueIdentifierDuplicate));
        assertEquals(valueIdentifier.hashCode(), valueIdentifierDuplicate.hashCode());
        assertEquals(valueIdentifier.getName(), valueIdentifierDuplicate.getName());
        assertEquals(valueIdentifier.getRInnerType(), valueIdentifierDuplicate.getRInnerType());

        assertFalse(valueIdentifier.equals(secondValueIdentifier));
        assertFalse(secondValueIdentifier.equals(valueIdentifierDuplicate));
        assertNotSame(valueIdentifier.hashCode(), secondValueIdentifier.hashCode());

        assertFalse(valueIdentifier.equals(otherValueIdentifier));
        assertFalse(otherValueIdentifier.equals(valueIdentifierDuplicate));
        assertNotSame(valueIdentifier.hashCode(), valueIdentifierDuplicate.hashCode());

        assertFalse(valueIdentifier.equals(this));

    }
}
