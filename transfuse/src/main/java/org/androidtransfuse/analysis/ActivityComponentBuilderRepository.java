package org.androidtransfuse.analysis;

import android.app.Activity;
import android.os.Bundle;
import org.androidtransfuse.gen.ComponentBuilder;
import org.androidtransfuse.gen.componentBuilder.*;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.util.Logger;

import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ActivityComponentBuilderRepository {

    private ComponentBuilderFactory componentBuilderFactory;
    private Map<String, Set<MethodCallbackGenerator>> activityGenerators;
    private Logger log;

    public ActivityComponentBuilderRepository(ComponentBuilderFactory componentBuilderFactory, Map<String, Set<MethodCallbackGenerator>> activityGenerators, Logger log) {
        this.componentBuilderFactory = componentBuilderFactory;
        this.activityGenerators = activityGenerators;
        this.log = log;
    }

    public ComponentBuilder buildComponentBuilder(String activityType, InjectionNode injectionNode, Integer layout) {
        //onCreate
        LayoutBuilder rLayoutBuilder;
        if (layout == null) {
            rLayoutBuilder = new NoOpLayoutBuilder();
        } else {
            rLayoutBuilder = componentBuilderFactory.buildRLayoutBuilder(layout);
        }
        OnCreateComponentBuilder onCreateComponentBuilder = componentBuilderFactory.buildOnCreateComponentBuilder(injectionNode, rLayoutBuilder, Bundle.class);

        if (activityGenerators.containsKey(activityType)) {
            onCreateComponentBuilder.addMethodCallbackBuilders(activityGenerators.get(activityType));
        } else {
            log.warning("Unable to access Activity type: " + activityType + ", defaulting to standard Activity");
            onCreateComponentBuilder.addMethodCallbackBuilders(activityGenerators.get(Activity.class.getName()));
        }

        return onCreateComponentBuilder;
    }
}
