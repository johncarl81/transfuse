/**
 * Copyright 2013 John Ericksen
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

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ServiceIntentFactoryStrategyTest {

    private ServiceIntentFactoryStrategy activityIntentFactoryStrategy;
    private Context mockContext;
    private Bundle mockBundle;
    private Intent mockIntent;

    @Before
    public void setup(){
        mockContext = new Activity();
        mockBundle = new Bundle();
        mockIntent = new Intent();
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
    }
}
