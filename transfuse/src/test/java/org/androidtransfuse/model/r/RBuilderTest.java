package org.androidtransfuse.model.r;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.config.TransfuseGenerationGuiceModule;
import org.androidtransfuse.util.JavaUtilLogger;
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
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new TransfuseGenerationGuiceModule(new JavaUtilLogger(this)));
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
