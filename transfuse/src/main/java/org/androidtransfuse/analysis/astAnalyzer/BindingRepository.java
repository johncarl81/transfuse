package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class BindingRepository {

    private Map<String, InjectionNodeBuilder> variableBuilderMap = new HashMap<String, InjectionNodeBuilder>();

    public void addVariableBuilder(String annotationName, InjectionNodeBuilder variableBuilder) {
        this.variableBuilderMap.put(annotationName, variableBuilder);
    }

    public boolean containsBindingVariableBuilder(ASTAnnotation bindingAnnotation) {
        return variableBuilderMap.containsKey(bindingAnnotation.getName());
    }

    public InjectionNodeBuilder getBindingVariableBuilder(ASTAnnotation bindingAnnotation) {
        return variableBuilderMap.get(bindingAnnotation.getName());
    }
}
