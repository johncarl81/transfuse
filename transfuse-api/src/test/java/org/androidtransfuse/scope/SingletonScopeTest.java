package org.androidtransfuse.scope;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Provider;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class SingletonScopeTest {

    private interface ScopeTargetBuilder extends Provider<ScopeTarget> {
    }

    private ScopeTarget scopeTarget;
    private ScopeTargetBuilder builder;
    private Scope singletonScope;

    @Before
    public void setup() {
        builder = EasyMock.createMock(ScopeTargetBuilder.class);
        singletonScope = SingletonScope.getInstance();
        scopeTarget = new ScopeTarget();
    }

    @Test
    public void testScopedBuild() {
        EasyMock.reset(builder);

        EasyMock.expect(builder.get()).andReturn(scopeTarget);

        EasyMock.replay(builder);

        ScopeTarget resultTarget = singletonScope.getScopedObject(ScopeTarget.class, builder);
        assertEquals(scopeTarget, resultTarget);
        ScopeTarget secondResultTarget = singletonScope.getScopedObject(ScopeTarget.class, builder);
        assertEquals(scopeTarget, secondResultTarget);

        EasyMock.verify(builder);
    }

    @Test
    public void testScopePerformance() {

        ScopeTargetBuilder perfBuilder = new ScopeTargetBuilder() {
            @Override
            public ScopeTarget get() {
                return new ScopeTarget();
            }
        };

        Scope perfSrouce = SingletonScope.getInstance();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            perfSrouce.getScopedObject(ScopeTarget.class, perfBuilder);
        }
        System.out.println("Total time: " + (System.currentTimeMillis() - start));
    }

    @Test
    public void verifyScopeMethod() throws NoSuchMethodException {

        Method getScopedObjectMethod = Scope.class.getMethod(Scope.GET_SCOPED_OBJECT, Class.class, Provider.class);

        assertNotNull(getScopedObjectMethod);
    }
}
