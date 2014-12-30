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

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

/**
 * Factory for building an Android `Intent` from an input `IntentStrategy`.
 *
 * @author John Ericksen
 */
public class IntentFactory {

    private final Context context;
    private final IntentAdapterFactory intentMockFactory;

    /**
     * Intent factory class used for testing purposes.
     */
    protected interface IntentAdapterFactory {

        /**
         * Builds an Intent using the given current context and target Context class.
         *
         * @param context current context
         * @param clazz target context class
         * @return Intent
         */
        Intent buildIntent(Context context, Class<? extends Context> clazz);
    }

    /**
     * Concrete default implementation of the IntentAdapterFactory.
     */
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

    /**
     * Start the appropriate target Context specified by the input Strategy.
     *
     * @param parameters Strategy instance
     */
    public void start(IntentFactoryStrategy parameters) {
        Intent intent = buildIntent(parameters);

        parameters.start(context, intent);

    }

    /**
     * Build an Intent specified by the given input Strategy.
     *
     * @param parameters Strategy instance
     * @return Intent
     */
    public Intent buildIntent(IntentFactoryStrategy parameters) {
        android.content.Intent intent = intentMockFactory.buildIntent(context, parameters.getTargetContext());
        intent.setFlags(parameters.getFlags());
        for (String category : parameters.getCategories()){
            intent.addCategory(category);
        }

        intent.putExtras(parameters.getExtras());

        return intent;
    }

    /**
     * Build a PendingIntent specified by the given input Strategy.
     *
     * @param parameters Strategy instance
     * @return PendingIntent
     */
    public PendingIntent buildPendingIntent(IntentFactoryStrategy parameters){
        return buildPendingIntent(0, parameters);
    }

    /**
     * Build a PendingIntent specified by the given input Strategy.
     *
     * @param requestCode request code for the sender
     * @param parameters Strategy instance
     * @return PendingIntent
     */
    public PendingIntent buildPendingIntent(int requestCode, IntentFactoryStrategy parameters){
        return buildPendingIntent(requestCode, 0, parameters);
    }

    /**
     * Build a PendingIntent specified by the given input Strategy.
     *
     * @param requestCode request code for the sender
     * @param flags intent flags
     * @param parameters Strategy instance
     * @return PendingIntent
     */
    public PendingIntent buildPendingIntent(int requestCode, int flags, IntentFactoryStrategy parameters){
        return PendingIntent.getActivity(context, requestCode, buildIntent(parameters), flags);
    }
}
