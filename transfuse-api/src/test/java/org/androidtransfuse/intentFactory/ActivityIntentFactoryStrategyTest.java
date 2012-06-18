package org.androidtransfuse.intentFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * @author John Ericksen
 */
public class ActivityIntentFactoryStrategyTest {

    private ActivityIntentFactoryStrategy activityIntentFactoryStrategy;
    private Context mockContext;
    private Bundle mockBundle;
    private Intent mockIntent;

    @Before
    public void setup(){
        mockContext = PowerMockito.mock(Context.class);
        mockBundle = PowerMockito.mock(Bundle.class);
        mockIntent = PowerMockito.mock(Intent.class);
        activityIntentFactoryStrategy = new ActivityIntentFactoryStrategy(Activity.class, mockBundle);
    }

    @Test
    public void testContext(){
        assertEquals(Activity.class, activityIntentFactoryStrategy.getTargetContext());
    }

    @Test
    public void testExtras(){
        assertEquals(mockBundle, activityIntentFactoryStrategy.getExtras());
    }

    @Test
    public void testStart(){
        activityIntentFactoryStrategy.start(mockContext, mockIntent);

        verify(mockContext).startActivity(mockIntent);
    }
}
