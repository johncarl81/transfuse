/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.intentFactory;

import android.content.Context;
import android.content.Intent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class IntentFactoryTest {

    private IntentFactory intentFactory;
    private IntentFactory.IntentAdapterFactory intentMockFactory;
    private Context mockContext;
    private IntentFactoryStrategy mockStrategy;
    private Intent mockIntent;

    @Before
    public void setup(){
        mockContext = Mockito.mock(Context.class);
        intentMockFactory = Mockito.mock(IntentFactory.IntentAdapterFactory.class);
        mockStrategy = Mockito.mock(IntentFactoryStrategy.class);
        mockIntent = Mockito.mock(Intent.class);

        intentFactory = new IntentFactory(mockContext, intentMockFactory);
    }

    @Test
    public void testBuildIntent(){
        Mockito.when(intentMockFactory.buildIntent(mockContext, mockStrategy.getTargetContext())).thenReturn(mockIntent);

        Intent outputIntent = intentFactory.buildIntent(mockStrategy);

        assertEquals(mockIntent, outputIntent);
        Mockito.verify(mockIntent).putExtras(mockStrategy.getExtras());
    }

    @Test
    public void testStart(){
        Mockito.when(intentMockFactory.buildIntent(mockContext, mockStrategy.getTargetContext())).thenReturn(mockIntent);

        intentFactory.start(mockStrategy);

        Mockito.verify(mockIntent).putExtras(mockStrategy.getExtras());
        Mockito.verify(mockStrategy).start(mockContext, mockIntent);
    }

}
