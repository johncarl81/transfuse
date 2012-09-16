package org.androidtransfuse.gen.variableBuilder;

import android.content.SharedPreferences;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Preference;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class PreferenceInjectionNodeBuilder extends InjectionNodeBuilderSingleAnnotationAdapter {

    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private final InjectionPointFactory injectionPointFactory;

    @Inject
    public PreferenceInjectionNodeBuilder(VariableInjectionBuilderFactory variableInjectionBuilderFactory, InjectionPointFactory injectionPointFactory) {
        super(Preference.class);
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.injectionPointFactory = injectionPointFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        String preferenceName = annotation.getProperty("value", String.class);

        InjectionNode injectionNode = new InjectionNode(astType);

        InjectionNode preferenceManagerInjectionNode = injectionPointFactory.buildInjectionNode(SharedPreferences.class, context);

        injectionNode.addAspect(VariableBuilder.class,
                variableInjectionBuilderFactory.buildPreferenceVariableBuilder(astType, preferenceName, preferenceManagerInjectionNode));

        return injectionNode;
    }
}
