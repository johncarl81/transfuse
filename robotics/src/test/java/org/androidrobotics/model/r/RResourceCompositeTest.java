package org.androidrobotics.model.r;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.util.JavaUtilLogger;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * @author John Ericksen
 */
public class RResourceCompositeTest {

    private static final int RESOURCE_THAT_DOESNT_EXIST = 12345;

    @Inject
    private ASTClassFactory astClassFactory;
    private RResourceComposite resourceComposite;
    private RResourceMapping resourceOne;
    private RResourceMapping resourceTwo;

    private static class RTest {
        public static final class one {
            public static final int id1 = 0x7f070003;
        }

        public static final class two {
            public static final int id2 = 0x7f070004;
        }
    }

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this)));
        injector.injectMembers(this);

        resourceOne = new RResourceMapping();
        resourceOne.addResource(astClassFactory.buildASTClassType(RTest.one.class), "id1", RTest.one.id1);

        resourceTwo = new RResourceMapping();
        resourceTwo.addResource(astClassFactory.buildASTClassType(RTest.two.class), "id2", RTest.two.id2);

        resourceComposite = new RResourceComposite(resourceOne, resourceTwo);
    }

    @Test
    public void testComposite() {
        ResourceIdentifier resourceIdOne = resourceComposite.getResourceIdentifier(RTest.one.id1);
        ResourceIdentifier resourceIdTwo = resourceComposite.getResourceIdentifier(RTest.two.id2);

        assertEquals(resourceOne.getResourceIdentifier(RTest.one.id1), resourceIdOne);
        assertEquals(resourceTwo.getResourceIdentifier(RTest.two.id2), resourceIdTwo);
        assertNull(resourceComposite.getResourceIdentifier(RESOURCE_THAT_DOESNT_EXIST));
    }
}
