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
import java.util.Collections;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class RBuilderTest {

    @Inject
    private RBuilder rBuilder;
    @Inject
    private ASTClassFactory astClassFactory;
    private ASTType idInnerType;

    public class RTest {
        public static final int id1 = 1;
    }

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this)));
        injector.injectMembers(this);

        idInnerType = astClassFactory.buildASTClassType(RTest.class);
    }

    @Test
    public void testBuild() {
        RResourceMapping rResourceMapping = rBuilder.buildR(Collections.singleton(idInnerType));

        ResourceIdentifier resourceIdentifier = rResourceMapping.getResourceIdentifier(RTest.id1);

        assertEquals("id1", resourceIdentifier.getName());
        assertEquals(idInnerType, resourceIdentifier.getRInnerType());
    }
}
