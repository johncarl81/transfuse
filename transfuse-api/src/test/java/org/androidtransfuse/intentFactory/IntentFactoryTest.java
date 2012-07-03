package org.androidtransfuse.intentFactory;

import android.content.Context;
import android.content.Intent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class IntentFactoryTest {

    private IntentFactory intentFactory;
    private IntentFactory.IntentAdapterFactory intentMockFactory;
    private Context mockCotext;
    private IntentFactoryStrategy mockStrategy;
    private Intent mockIntent;

    @Before
    public void setup(){
        mockCotext = Mockito.mock(Context.class);
        intentMockFactory = Mockito.mock(IntentFactory.IntentAdapterFactory.class);
        mockStrategy = Mockito.mock(IntentFactoryStrategy.class);
        mockIntent = Mockito.mock(Intent.class);

        intentFactory = new IntentFactory(mockCotext, intentMockFactory);
    }

    @Test
    public void testBuildIntent(){
        Mockito.when(intentMockFactory.buildIntent(mockCotext, mockStrategy.getTargetContext())).thenReturn(mockIntent);

        Intent outputIntent = intentFactory.buildIntent(mockStrategy);

        assertEquals(mockIntent, outputIntent);
        Mockito.verify(mockIntent).putExtras(mockStrategy.getExtras());
    }

    @Test
    public void testStart(){
        Mockito.when(intentMockFactory.buildIntent(mockCotext, mockStrategy.getTargetContext())).thenReturn(mockIntent);

        intentFactory.start(mockStrategy);

        Mockito.verify(mockIntent).putExtras(mockStrategy.getExtras());
        Mockito.verify(mockStrategy).start(mockCotext, mockIntent);
    }

}
