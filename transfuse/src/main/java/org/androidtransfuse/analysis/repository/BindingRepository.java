package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository holding the variety of InjectionNodeBuilders associated with specific binding annotations:
 * (ie: @Extra -> ExtraInjectionNodeBuilder, @SystemService -> SystemServiceInjectionNodeBuilder),
 *
 * @author John Ericksen
 */
public class BindingRepository {

    private Map<ASTType, InjectionNodeBuilder> variableBuilderMap = new HashMap<ASTType, InjectionNodeBuilder>();

    public void addVariableBuilder(ASTType annotation, InjectionNodeBuilder variableBuilder) {
        this.variableBuilderMap.put(annotation, variableBuilder);
    }

    public boolean containsBindingVariableBuilder(ASTAnnotation bindingAnnotation) {
        return variableBuilderMap.containsKey(bindingAnnotation.getASTType());
    }

    public InjectionNodeBuilder getBindingVariableBuilder(ASTAnnotation bindingAnnotation) {
        return variableBuilderMap.get(bindingAnnotation.getASTType());
    }
}
