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

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.config.TransfuseGenerateGuiceModule;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.util.matcher.Matcher;
import org.androidtransfuse.util.matcher.Matchers;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionNodeBuilderRepository implements InjectionNodeBuilder{

    private final Map<Matcher<InjectionSignature>, InjectionNodeBuilder> typeQualifierBindings = new HashMap<Matcher<InjectionSignature>, InjectionNodeBuilder>();
    private final Map<Matcher<ASTType>, InjectionNodeBuilder> typeBindings = new HashMap<Matcher<ASTType>, InjectionNodeBuilder>();
    private final InjectionNodeBuilder defaultBinding;
    private final ASTClassFactory astClassFactory;

    @Inject
    public InjectionNodeBuilderRepository(
            @Named(TransfuseGenerateGuiceModule.DEFAULT_BINDING) InjectionNodeBuilder defaultBinding,
            ASTClassFactory astClassFactory) {
        this.defaultBinding = defaultBinding;
        this.astClassFactory = astClassFactory;
    }

    public void putAnnotation(Class<?> viewClass, InjectionNodeBuilder viewVariableBuilder) {
        putSignatureMatcher(Matchers.annotated().byType(astClassFactory.getType(viewClass)).build(), viewVariableBuilder);
    }

    public void putType(ASTType type, InjectionNodeBuilder variableBuilder) {
        putTypeMatcher(Matchers.type(type).build(), variableBuilder);
    }

    public void putType(Class<?> clazz, InjectionNodeBuilder variableBuilder) {
        putTypeMatcher(Matchers.type(astClassFactory.getType(clazz)).build(), variableBuilder);
    }

    public void putTypeMatcher(Matcher<ASTType> matcher, InjectionNodeBuilder variableBuilder) {
        this.typeBindings.put(matcher, variableBuilder);
    }

    public void putSignatureMatcher(Matcher<InjectionSignature> matcher, InjectionNodeBuilder variableBuilder) {
        this.typeQualifierBindings.put(matcher, variableBuilder);
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, Collection<ASTAnnotation> qualifiers) {
        //check type and qualifiers
        InjectionNodeBuilder typeQualifierBuilder = get(typeQualifierBindings, new InjectionSignature(astType, qualifiers));

        if(typeQualifierBuilder != null){
            return typeQualifierBuilder.buildInjectionNode(astType, context, qualifiers);
        }

        if(qualifiers.size() > 0){
            throw new TransfuseAnalysisException("Type Annotation Qualifiers don't match any configuration");
        }

        //check type
        InjectionNodeBuilder typeBindingBuilder = get(typeBindings, astType);

        if (typeBindingBuilder != null) {
            return typeBindingBuilder.buildInjectionNode(astType, context, qualifiers);
        }

        //default case
        return defaultBinding.buildInjectionNode(astType, context, qualifiers);
    }

    private <T> InjectionNodeBuilder get(Map<Matcher<T>, InjectionNodeBuilder> builderMap, T input){
        InjectionNodeBuilder builder = null;
        for (Map.Entry<Matcher<T>, InjectionNodeBuilder> bindingEntry : builderMap.entrySet()) {
            if (bindingEntry.getKey().matches(input)) {
                if (builder != null) {
                    throw new TransfuseAnalysisException("Multiple types matched on type " + input);
                }
                builder = bindingEntry.getValue();
            }
        }
        return builder;
    }
}
