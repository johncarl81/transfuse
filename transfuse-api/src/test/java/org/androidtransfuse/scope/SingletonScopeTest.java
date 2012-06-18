package org.androidtransfuse.scope;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Provider;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        builder = mock(ScopeTargetBuilder.class);
        singletonScope = SingletonScope.getInstance();
        scopeTarget = new ScopeTarget();
    }

    @Test
    public void testScopedBuild() {
        when(builder.get()).thenReturn(scopeTarget);

        ScopeTarget resultTarget = singletonScope.getScopedObject(ScopeTarget.class, builder);
        assertEquals(scopeTarget, resultTarget);
        ScopeTarget secondResultTarget = singletonScope.getScopedObject(ScopeTarget.class, builder);
        assertEquals(scopeTarget, secondResultTarget);
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
