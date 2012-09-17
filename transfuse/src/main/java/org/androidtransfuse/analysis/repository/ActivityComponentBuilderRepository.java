package org.androidtransfuse.analysis.repository;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.gen.componentBuilder.ExpressionVariableDependentGenerator;

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
            return activityGenerators.get(android.app.Activity.class.getName());
        }
    }
}
