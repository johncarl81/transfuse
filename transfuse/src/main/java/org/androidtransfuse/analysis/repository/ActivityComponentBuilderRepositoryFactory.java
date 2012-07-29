package org.androidtransfuse.analysis.repository;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.ListActivity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.ListView;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.gen.GeneratorFactory;
import org.androidtransfuse.gen.componentBuilder.*;
import org.androidtransfuse.intentFactory.ActivityIntentFactoryStrategy;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ActivityComponentBuilderRepositoryFactory {

    private ComponentBuilderFactory componentBuilderFactory;
    private ASTClassFactory astClassFactory;
    private GeneratorFactory generatorFactory;
    private ListenerRegistrationGenerator listenerRegistrationGenerator;
    private NonConfigurationInstanceGenerator nonConfigurationInstanceGenerator;

    @Inject
    public ActivityComponentBuilderRepositoryFactory(ASTClassFactory astClassFactory,
                                                     ComponentBuilderFactory componentBuilderFactory,
                                                     GeneratorFactory generatorFactory,
                                                     ListenerRegistrationGenerator listenerRegistrationGenerator,
                                                     NonConfigurationInstanceGenerator nonConfigurationInstanceGenerator) {
        this.astClassFactory = astClassFactory;
        this.componentBuilderFactory = componentBuilderFactory;
        this.generatorFactory = generatorFactory;
        this.listenerRegistrationGenerator = listenerRegistrationGenerator;
        this.nonConfigurationInstanceGenerator = nonConfigurationInstanceGenerator;
    }

    public ActivityComponentBuilderRepository build(AnalysisContext context) {

        Map<String, Set<ExpressionVariableDependentGenerator>> methodCallbackGenerators = new HashMap<String, Set<ExpressionVariableDependentGenerator>>();

        Set<ExpressionVariableDependentGenerator> activityMethodGenerators = buildActivityMethodCallbackGenerators();
        methodCallbackGenerators.put(Activity.class.getName(), activityMethodGenerators);
        methodCallbackGenerators.put(ListActivity.class.getName(), buildListActivityMethodCallbackGenerators(activityMethodGenerators));
        methodCallbackGenerators.put(PreferenceActivity.class.getName(), activityMethodGenerators);
        methodCallbackGenerators.put(ActivityGroup.class.getName(), activityMethodGenerators);

        return new ActivityComponentBuilderRepository(methodCallbackGenerators);
    }

    private Set<ExpressionVariableDependentGenerator> buildListActivityMethodCallbackGenerators(Set<ExpressionVariableDependentGenerator> activityMethodGenerators) {
        Set<ExpressionVariableDependentGenerator> listActivityCallbackGenerators = new HashSet<ExpressionVariableDependentGenerator>();
        listActivityCallbackGenerators.addAll(activityMethodGenerators);

        //onListItemClick(android.widget.ListView l, android.view.View v, int position, long id)
        ASTMethod onListItemClickMethod = getASTMethod(ListActivity.class, "onListItemClick", ListView.class, View.class, int.class, long.class);
        listActivityCallbackGenerators.add(
                componentBuilderFactory.buildMethodCallbackGenerator("onListItemClick",
                        componentBuilderFactory.buildMirroredMethodGenerator(onListItemClickMethod, false)));

        return listActivityCallbackGenerators;
    }

    private Set<ExpressionVariableDependentGenerator> buildActivityMethodCallbackGenerators() {
        Set<ExpressionVariableDependentGenerator> activityCallbackGenerators = new HashSet<ExpressionVariableDependentGenerator>();
        // onDestroy
        activityCallbackGenerators.add(buildEventMethod("onDestroy"));
        // onPause
        activityCallbackGenerators.add(buildEventMethod("onPause"));
        // onRestart
        activityCallbackGenerators.add(buildEventMethod("onRestart"));
        // onResume
        activityCallbackGenerators.add(buildEventMethod("onResume"));
        // onStart
        activityCallbackGenerators.add(buildEventMethod("onStart"));
        // onStop
        activityCallbackGenerators.add(buildEventMethod("onStop"));
        // onBackPressed
        activityCallbackGenerators.add(buildEventMethod("onBackPressed"));

        // onSaveInstanceState
        ASTMethod onSaveIntanceStateMethod = getASTMethod("onSaveInstanceState", Bundle.class);
        activityCallbackGenerators.add(
                componentBuilderFactory.buildMethodCallbackGenerator("onSaveInstanceState",
                        componentBuilderFactory.buildMirroredMethodGenerator(onSaveIntanceStateMethod, true)));

        // onRestoreInstanceState
        ASTMethod onRestoreInstanceState = getASTMethod("onRestoreInstanceState", Bundle.class);
        activityCallbackGenerators.add(
                componentBuilderFactory.buildMethodCallbackGenerator("onRestoreInstanceState",
                        componentBuilderFactory.buildMirroredMethodGenerator(onRestoreInstanceState, true)));

        //extra intent factory
        activityCallbackGenerators.add(generatorFactory.buildStrategyGenerator(ActivityIntentFactoryStrategy.class));

        //listener registration
        activityCallbackGenerators.add(listenerRegistrationGenerator);

        //non configuration instance update
        activityCallbackGenerators.add(nonConfigurationInstanceGenerator);

        return activityCallbackGenerators;
    }


    private MethodCallbackGenerator buildEventMethod(String name) {
        return buildEventMethod(name, name);
    }

    private MethodCallbackGenerator buildEventMethod(String eventName, String methodName) {

        ASTMethod method = getASTMethod(methodName);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventName,
                componentBuilderFactory.buildMirroredMethodGenerator(method, true));
    }

    private ASTMethod getASTMethod(String methodName, Class... args){
        return getASTMethod(Activity.class, methodName, args);
    }

    private ASTMethod getASTMethod(Class type, String methodName, Class... args){
        try{
            return astClassFactory.buildASTClassMethod(type.getDeclaredMethod(methodName, args));
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while trying to reference method " + methodName, e);
        }
    }
}
