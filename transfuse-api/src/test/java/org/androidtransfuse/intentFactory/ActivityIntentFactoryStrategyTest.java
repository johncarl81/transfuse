package org.androidtransfuse.intentFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import static junit.framework.Assert.assertEquals;

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
        mockContext = PowerMock.createMock(Context.class);
        mockBundle = PowerMock.createMock(Bundle.class);
        mockIntent = PowerMock.createMock(Intent.class);
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
    public void test(){
        EasyMock.reset(mockBundle, mockIntent, mockContext);

        mockContext.startActivity(mockIntent);

        EasyMock.replay(mockBundle, mockIntent, mockContext);

        activityIntentFactoryStrategy.start(mockContext, mockIntent);

        EasyMock.verify(mockBundle, mockIntent, mockContext);
    }
}
