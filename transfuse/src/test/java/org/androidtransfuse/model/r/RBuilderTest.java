package org.androidtransfuse.model.r;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
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
        TransfuseTestInjector.inject(this);

        idInnerType = astClassFactory.getType(RTest.class);
    }

    @Test
    public void testBuild() {
        RResourceMapping rResourceMapping = rBuilder.buildR(Collections.singleton(idInnerType));

        ResourceIdentifier resourceIdentifier = rResourceMapping.getResourceIdentifier(RTest.id1);

        assertEquals("id1", resourceIdentifier.getName());
        assertEquals(idInnerType, resourceIdentifier.getRInnerType());
    }
}
