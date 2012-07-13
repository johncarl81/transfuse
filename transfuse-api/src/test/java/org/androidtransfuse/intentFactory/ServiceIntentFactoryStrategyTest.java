package org.androidtransfuse.intentFactory;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * @author John Ericksen
 */
public class ServiceIntentFactoryStrategyTest {

    private ServiceIntentFactoryStrategy activityIntentFactoryStrategy;
    private Context mockContext;
    private Bundle mockBundle;
    private Intent mockIntent;

    @Before
    public void setup(){
        mockContext = mock(Context.class);
        mockBundle = mock(Bundle.class);
        mockIntent = mock(Intent.class);
        activityIntentFactoryStrategy = new ServiceIntentFactoryStrategy(Service.class, mockBundle);
    }

    @Test
    public void testContext(){
        assertEquals(Service.class, activityIntentFactoryStrategy.getTargetContext());
    }

    @Test
    public void testExtras(){
        assertEquals(mockBundle, activityIntentFactoryStrategy.getExtras());
    }

    @Test
    public void testStart(){
        activityIntentFactoryStrategy.start(mockContext, mockIntent);

        verify(mockContext).startService(mockIntent);
    }
}
