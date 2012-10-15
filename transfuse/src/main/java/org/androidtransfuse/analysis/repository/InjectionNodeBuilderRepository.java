package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.util.matcher.ASTMatcherBuilder;
import org.androidtransfuse.util.matcher.Matcher;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionNodeBuilderRepository {

    private final Map<ASTType, InjectionNodeBuilder> bindingAnnotations = new HashMap<ASTType, InjectionNodeBuilder>();
    private final Map<Matcher<ASTType>, InjectionNodeBuilder> typeBindings = new HashMap<Matcher<ASTType>, InjectionNodeBuilder>();
    private final InjectionNodeBuilder defaultBinding;
    private final ASTClassFactory astClassFactory;
    private final ASTMatcherBuilder astMatcherBuilder;

    @Inject
    public InjectionNodeBuilderRepository(@Named("defaultBinding") InjectionNodeBuilder defaultBinding, ASTClassFactory astClassFactory, ASTMatcherBuilder astMatcherBuilder){
        this.defaultBinding = defaultBinding;
        this.astClassFactory = astClassFactory;
        this.astMatcherBuilder = astMatcherBuilder;
    }

    public void putAnnotation(ASTType annotationType, InjectionNodeBuilder annotatedVariableBuilder){
        bindingAnnotations.put(annotationType, annotatedVariableBuilder);
    }

    public void putAnnotation(Class<?> viewClass, InjectionNodeBuilder viewVariableBuilder) {
        putAnnotation(astClassFactory.buildASTClassType(viewClass), viewVariableBuilder);
    }

    public void putType(ASTType type, InjectionNodeBuilder variableBuilder) {
        putMatcher(astMatcherBuilder.type(type).build(), variableBuilder);
    }

    public void putType(Class<?> clazz, InjectionNodeBuilder variableBuilder) {
        putMatcher(astMatcherBuilder.type(clazz).build(), variableBuilder);
    }

    public void putMatcher(Matcher<ASTType> matcher, InjectionNodeBuilder variableBuilder) {
        this.typeBindings.put(matcher, variableBuilder);
    }

    public boolean containsBinding(ASTAnnotation bindingAnnotation) {
        return bindingAnnotations.containsKey(bindingAnnotation.getASTType());
    }

    public InjectionNodeBuilder getBinding(ASTAnnotation bindingAnnotation) {
        return bindingAnnotations.get(bindingAnnotation.getASTType());
    }

    public InjectionNodeBuilder getBinding(ASTType type) {
        for (Map.Entry< Matcher<ASTType>, InjectionNodeBuilder> bindingEntry : typeBindings.entrySet()) {
            if(bindingEntry.getKey().matches(type)){
                return bindingEntry.getValue();
            }
        }
        return defaultBinding;
    }
}
