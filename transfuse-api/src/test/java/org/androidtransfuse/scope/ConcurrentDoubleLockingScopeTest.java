package org.androidtransfuse.scope;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Provider;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author John Ericksen
 */
public class ConcurrentDoubleLockingScopeTest {

    private interface ScopeTargetBuilder extends Provider<ScopeTarget> {
    }

    private ScopeTarget scopeTarget;
    private ScopeTargetBuilder builder;
    private Scope scope;

    @Before
    public void setup() {
        builder = mock(ScopeTargetBuilder.class);
        scope = new ConcurrentDoubleLockingScope();
        scopeTarget = new ScopeTarget();
    }

    @Test
    public void testScopedBuild() {
        when(builder.get()).thenReturn(scopeTarget);

        ScopeTarget resultTarget = scope.getScopedObject(ScopeTarget.class, builder);
        assertEquals(scopeTarget, resultTarget);
        ScopeTarget secondResultTarget = scope.getScopedObject(ScopeTarget.class, builder);
        assertEquals(scopeTarget, secondResultTarget);
    }

}
