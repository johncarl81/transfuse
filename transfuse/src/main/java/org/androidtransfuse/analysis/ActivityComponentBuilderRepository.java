package org.androidtransfuse.analysis;

import android.app.Activity;
import android.os.Bundle;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
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
    private Map<String, Set<ExpressionVariableDependentGenerator>> activityGenerators;
    private Logger log;
    private ASTClassFactory astClassFactory;

    public ActivityComponentBuilderRepository(ComponentBuilderFactory componentBuilderFactory, Map<String, Set<ExpressionVariableDependentGenerator>> activityGenerators, Logger log, ASTClassFactory astClassFactory) {
        this.componentBuilderFactory = componentBuilderFactory;
        this.activityGenerators = activityGenerators;
        this.log = log;
        this.astClassFactory = astClassFactory;
    }

    public ComponentBuilder buildComponentBuilder(String activityType, InjectionNode injectionNode, Integer layout, InjectionNode layoutHandlerInjectionNode) {
        //onCreate
        LayoutBuilder layoutBuilder;
        if (layout == null) {
            if (layoutHandlerInjectionNode == null) {
                layoutBuilder = new NoOpLayoutBuilder();
            } else {
                layoutBuilder = componentBuilderFactory.buildLayoutHandlerBuilder(layoutHandlerInjectionNode);
            }
        } else {
            layoutBuilder = componentBuilderFactory.buildRLayoutBuilder(layout);
        }
        try {

            ASTMethod onCreateASTMethod = astClassFactory.buildASTClassMethod(Activity.class.getDeclaredMethod("onCreate", Bundle.class));

            OnCreateComponentBuilder onCreateComponentBuilder = componentBuilderFactory.buildOnCreateComponentBuilder(injectionNode, layoutBuilder, onCreateASTMethod);

            if (activityGenerators.containsKey(activityType)) {
                onCreateComponentBuilder.addMethodCallbackBuilders(activityGenerators.get(activityType));
            } else {
                log.warning("Unable to access Activity type: " + activityType + ", defaulting to standard Activity");
                onCreateComponentBuilder.addMethodCallbackBuilders(activityGenerators.get(Activity.class.getName()));
            }

            return onCreateComponentBuilder;

        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while trying to build event method", e);
        }
    }
}
