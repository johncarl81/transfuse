package org.androidrobotics.analysis.astAnalyzer;

import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.gen.variableBuilder.AnnotatedVariableBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class BindingRepository {

    Map<String, AnnotatedVariableBuilder> variableBuilderMap = new HashMap<String, AnnotatedVariableBuilder>();

    public void addVariableBuilder(String annotationName, AnnotatedVariableBuilder variableBuilder) {
        this.variableBuilderMap.put(annotationName, variableBuilder);
    }

    public boolean containsBindingVariableBuilder(ASTAnnotation bindingAnnotation) {
        return variableBuilderMap.containsKey(bindingAnnotation.getName());
    }

    public AnnotatedVariableBuilder getBindingVariableBuilder(ASTAnnotation bindingAnnotation) {
        return variableBuilderMap.get(bindingAnnotation.getName());
    }
}
