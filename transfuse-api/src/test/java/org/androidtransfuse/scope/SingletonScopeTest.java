package org.androidtransfuse.scope;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Provider;

import static junit.framework.Assert.assertEquals;

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
        singletonScope = new SingletonScope();
        scopeTarget = new ScopeTarget();
    }

    @Test
    public void testScopedBuild() {
        EasyMock.reset(builder);

        EasyMock.expect(builder.get()).andReturn(scopeTarget);

        EasyMock.replay(builder);

        ScopeTarget resultTarget = (ScopeTarget) singletonScope.getScopedObject(ScopeTarget.class, builder);
        assertEquals(scopeTarget, resultTarget);
        ScopeTarget secondResultTarget = (ScopeTarget) singletonScope.getScopedObject(ScopeTarget.class, builder);
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

        Scope perfSrouce = new SingletonScope();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            perfSrouce.getScopedObject(ScopeTarget.class, perfBuilder);
        }
        System.out.println("Total time: " + (System.currentTimeMillis() - start));
    }
}
