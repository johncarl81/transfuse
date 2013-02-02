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

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.ListActivity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.ListView;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.GeneratorFactory;
import org.androidtransfuse.gen.componentBuilder.*;
import org.androidtransfuse.intentFactory.ActivityIntentFactoryStrategy;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ActivityComponentBuilderRepositoryFactory {

    private final ComponentBuilderFactory componentBuilderFactory;
    private final ASTClassFactory astClassFactory;
    private final GeneratorFactory generatorFactory;
    private final ListenerRegistrationGenerator listenerRegistrationGenerator;
    private final NonConfigurationInstanceGenerator nonConfigurationInstanceGenerator;

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

    public ActivityComponentBuilderRepository build() {

        ImmutableMap.Builder<String, ImmutableSet<ExpressionVariableDependentGenerator>> methodCallbackGenerators = ImmutableMap.builder();

        ImmutableSet<ExpressionVariableDependentGenerator> activityMethodGenerators = buildActivityMethodCallbackGenerators();
        methodCallbackGenerators.put(Activity.class.getName(), activityMethodGenerators);
        methodCallbackGenerators.put(ListActivity.class.getName(), buildListActivityMethodCallbackGenerators(activityMethodGenerators));
        methodCallbackGenerators.put(PreferenceActivity.class.getName(), activityMethodGenerators);
        methodCallbackGenerators.put(ActivityGroup.class.getName(), activityMethodGenerators);

        return new ActivityComponentBuilderRepository(methodCallbackGenerators.build());
    }

    private ImmutableSet<ExpressionVariableDependentGenerator> buildListActivityMethodCallbackGenerators(Set<ExpressionVariableDependentGenerator> activityMethodGenerators) {
        ImmutableSet.Builder<ExpressionVariableDependentGenerator> listActivityCallbackGenerators = ImmutableSet.builder();
        listActivityCallbackGenerators.addAll(activityMethodGenerators);

        //onListItemClick(android.widget.ListView l, android.view.View v, int position, long id)
        ASTMethod onListItemClickMethod = getASTMethod(ListActivity.class, "onListItemClick", ListView.class, View.class, int.class, long.class);
        listActivityCallbackGenerators.add(
                componentBuilderFactory.buildMethodCallbackGenerator(astClassFactory.getType(OnListItemClick.class),
                        componentBuilderFactory.buildMirroredMethodGenerator(onListItemClickMethod, false)));

        return listActivityCallbackGenerators.build();
    }

    private ImmutableSet<ExpressionVariableDependentGenerator> buildActivityMethodCallbackGenerators() {
        ImmutableSet.Builder<ExpressionVariableDependentGenerator> activityCallbackGenerators = ImmutableSet.builder();
        // onDestroy
        activityCallbackGenerators.add(buildEventMethod(OnDestroy.class, "onDestroy"));
        // onPause
        activityCallbackGenerators.add(buildEventMethod(OnPause.class, "onPause"));
        // onRestart
        activityCallbackGenerators.add(buildEventMethod(OnRestart.class, "onRestart"));
        // onResume
        activityCallbackGenerators.add(buildEventMethod(OnResume.class, "onResume"));
        // onStart
        activityCallbackGenerators.add(buildEventMethod(OnStart.class, "onStart"));
        // onStop
        activityCallbackGenerators.add(buildEventMethod(OnStop.class, "onStop"));
        // onBackPressed
        activityCallbackGenerators.add(buildEventMethod(OnBackPressed.class, "onBackPressed"));

        // onSaveInstanceState
        ASTMethod onSaveInstanceStateMethod = getASTMethod("onSaveInstanceState", Bundle.class);
        activityCallbackGenerators.add(
                componentBuilderFactory.buildMethodCallbackGenerator(astClassFactory.getType(OnSaveInstanceState.class),
                        componentBuilderFactory.buildMirroredMethodGenerator(onSaveInstanceStateMethod, true)));

        // onRestoreInstanceState
        ASTMethod onRestoreInstanceState = getASTMethod("onRestoreInstanceState", Bundle.class);
        activityCallbackGenerators.add(
                componentBuilderFactory.buildMethodCallbackGenerator(astClassFactory.getType(OnRestoreInstanceState.class),
                        componentBuilderFactory.buildMirroredMethodGenerator(onRestoreInstanceState, true)));

        //extra intent factory
        activityCallbackGenerators.add(generatorFactory.buildStrategyGenerator(ActivityIntentFactoryStrategy.class));

        //listener registration
        activityCallbackGenerators.add(listenerRegistrationGenerator);

        //non configuration instance update
        activityCallbackGenerators.add(nonConfigurationInstanceGenerator);

        return activityCallbackGenerators.build();
    }

    private MethodCallbackGenerator buildEventMethod(Class<? extends Annotation> eventAnnotationClass, String methodName) {

        ASTMethod method = getASTMethod(methodName);
        ASTType eventAnnotation = astClassFactory.getType(eventAnnotationClass);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventAnnotation,
                componentBuilderFactory.buildMirroredMethodGenerator(method, true));
    }

    private ASTMethod getASTMethod(String methodName, Class... args) {
        return getASTMethod(Activity.class, methodName, args);
    }

    private ASTMethod getASTMethod(Class type, String methodName, Class... args) {
        try {
            return astClassFactory.getMethod(type.getDeclaredMethod(methodName, args));
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while trying to reference method " + methodName, e);
        }
    }
}
