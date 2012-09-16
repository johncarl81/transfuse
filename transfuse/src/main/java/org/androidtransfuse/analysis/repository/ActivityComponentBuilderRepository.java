package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.gen.componentBuilder.ExpressionVariableDependentGenerator;

import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ActivityComponentBuilderRepository {

    private final Map<String, Set<ExpressionVariableDependentGenerator>> activityGenerators;

    public ActivityComponentBuilderRepository(Map<String, Set<ExpressionVariableDependentGenerator>> activityGenerators) {
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
