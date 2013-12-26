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
package org.androidtransfuse.analysis.repository;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.gen.componentBuilder.ExpressionVariableDependentGenerator;
import org.androidtransfuse.util.AndroidLiterals;

import java.util.Set;

/**
 * @author John Ericksen
 */
public class ActivityComponentBuilderRepository {

    private final ImmutableMap<String, ImmutableSet<ExpressionVariableDependentGenerator>> activityGenerators;

    public ActivityComponentBuilderRepository(ImmutableMap<String, ImmutableSet<ExpressionVariableDependentGenerator>> activityGenerators) {
        this.activityGenerators = activityGenerators;
    }

    public Set<ExpressionVariableDependentGenerator> getGenerators(String key) {
        if (activityGenerators.containsKey(key)) {
            return activityGenerators.get(key);
        } else {
            //default
            return activityGenerators.get(AndroidLiterals.ACTIVITY.getName());
        }
    }
}
