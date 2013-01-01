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

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class IntentFactory {

    private final Context context;
    private final IntentAdapterFactory intentMockFactory;

    protected interface IntentAdapterFactory {
        Intent buildIntent(Context context, Class<? extends Context> clazz);
    }

    private static final class IntentAdapterFactoryImpl implements IntentAdapterFactory{
        @Override
        public Intent buildIntent(Context context, Class<? extends Context> clazz) {
            return new android.content.Intent(context, clazz);
        }
    }

    @Inject
    public IntentFactory(Context context) {
        this(context, new IntentAdapterFactoryImpl());
    }

    /**
     * Constructor used for testing purposes.
     *
     * @param context
     * @param intentMockFactory
     */
    protected IntentFactory(Context context, IntentAdapterFactory intentMockFactory){
        this.context = context;
        this.intentMockFactory = intentMockFactory;
    }

    public void start(IntentFactoryStrategy parameters) {
        Intent intent = buildIntent(parameters);

        parameters.start(context, intent);

    }

    public Intent buildIntent(IntentFactoryStrategy parameters) {
        android.content.Intent intent = intentMockFactory.buildIntent(context, parameters.getTargetContext());

        intent.putExtras(parameters.getExtras());

        return intent;
    }

    public PendingIntent buildPendingIntent(IntentFactoryStrategy parameters){
        return PendingIntent.getActivity(context, 0, buildIntent(parameters), 0);
    }
}
