package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionNodeBuilderRepository {

    private final Map<ASTType, InjectionNodeBuilder> bindingAnnotations = new HashMap<ASTType, InjectionNodeBuilder>();
    private final Map<ASTType, InjectionNodeBuilder> typeBindings = new HashMap<ASTType, InjectionNodeBuilder>();
    private final InjectionNodeBuilder defaultBinding;
    private final ASTClassFactory astClassFactory;

    @Inject
    public InjectionNodeBuilderRepository(@Named("defaultBinding") InjectionNodeBuilder defaultBinding, ASTClassFactory astClassFactory){
        this.defaultBinding = defaultBinding;
        this.astClassFactory = astClassFactory;
    }

    public void putAnnotation(ASTType annotationType, InjectionNodeBuilder annotatedVariableBuilder){
        bindingAnnotations.put(annotationType, annotatedVariableBuilder);
    }

    public void putAnnotation(Class<?> viewClass, InjectionNodeBuilder viewVariableBuilder) {
        putAnnotation(astClassFactory.buildASTClassType(viewClass), viewVariableBuilder);
    }

    public void putType(ASTType type, InjectionNodeBuilder variableBuilder) {
        typeBindings.put(type, variableBuilder);
    }

    public void putType(Class<?> clazz, InjectionNodeBuilder variableBuilder) {
        putType(astClassFactory.buildASTClassType(clazz), variableBuilder);
    }

    public boolean containsBinding(ASTAnnotation bindingAnnotation) {
        return bindingAnnotations.containsKey(bindingAnnotation.getASTType());
    }

    public InjectionNodeBuilder getBinding(ASTAnnotation bindingAnnotation) {
        return bindingAnnotations.get(bindingAnnotation.getASTType());
    }

    public InjectionNodeBuilder getBinding(ASTType type) {
        if(typeBindings.containsKey(type)){
            return typeBindings.get(type);
        }
        return defaultBinding;
    }
}
