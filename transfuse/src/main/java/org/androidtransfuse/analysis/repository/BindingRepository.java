package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository holding the variety of InjectionNodeBuilders associated with specific binding annotations:
 * (ie: @Extra -> ExtraInjectionNodeBuilder, @SystemService -> SystemServiceInjectionNodeBuilder),
 *
 * @author John Ericksen
 */
public class BindingRepository {

    private Map<String, InjectionNodeBuilder> variableBuilderMap = new HashMap<String, InjectionNodeBuilder>();

    public void addVariableBuilder(Class<? extends Annotation> annotation, InjectionNodeBuilder variableBuilder) {
        this.variableBuilderMap.put(annotation.getName(), variableBuilder);
    }

    public boolean containsBindingVariableBuilder(ASTAnnotation bindingAnnotation) {
        return variableBuilderMap.containsKey(bindingAnnotation.getName());
    }

    public InjectionNodeBuilder getBindingVariableBuilder(ASTAnnotation bindingAnnotation) {
        return variableBuilderMap.get(bindingAnnotation.getName());
    }
}
