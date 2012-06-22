package org.androidtransfuse.intentFactory;

import android.content.Context;
import android.content.Intent;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class IntentFactoryStrategyTest {

    @Test
    public void verifyMethods() throws NoSuchMethodException {
        Method getExtrasMethod = IntentFactoryStrategy.class.getMethod(IntentFactoryStrategy.GET_EXTRAS_METHOD);
        assertNotNull(getExtrasMethod);
        Method getTargetContext = IntentFactoryStrategy.class.getMethod(IntentFactoryStrategy.GET_TARGET_CONTEXT_METHOD);
        assertNotNull(getTargetContext);
        Method startMethod = IntentFactoryStrategy.class.getMethod(IntentFactoryStrategy.START_METHOD, Context.class, Intent.class);
        assertNotNull(startMethod);
    }
}
