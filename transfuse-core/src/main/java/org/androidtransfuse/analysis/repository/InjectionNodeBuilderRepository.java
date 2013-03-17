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

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.gen.scopeBuilder.ScopeAspectFactory;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.util.matcher.Matcher;
import org.androidtransfuse.util.matcher.Matchers;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class InjectionNodeBuilderRepository {

    private final Map<Matcher<InjectionSignature>, InjectionNodeBuilder> typeQualifierBindings = new HashMap<Matcher<InjectionSignature>, InjectionNodeBuilder>();
    private final Map<InjectionSignature, InjectionNodeBuilder> typeBindings = new HashMap<InjectionSignature, InjectionNodeBuilder>();
    private final Map<ASTType, ScopeAspectFactory> scopeVariableBuilderMap = new HashMap<ASTType, ScopeAspectFactory>();
    private final Map<ASTType, ASTType> scopeAnnotations = new HashMap<ASTType, ASTType>();
    private final Map<ASTType, ASTType> scoping = new HashMap<ASTType, ASTType>();
    private final ASTClassFactory astClassFactory;

    @Inject
    public InjectionNodeBuilderRepository(ASTClassFactory astClassFactory) {
        this.astClassFactory = astClassFactory;
    }

    public void putAnnotation(Class<?> viewClass, InjectionNodeBuilder viewVariableBuilder) {
        putSignatureMatcher(Matchers.annotated().byType(astClassFactory.getType(viewClass)).build(), viewVariableBuilder);
    }

    public void putType(ASTType type, InjectionNodeBuilder variableBuilder) {
        putType(new InjectionSignature(type), variableBuilder);
    }

    public void putType(Class<?> clazz, InjectionNodeBuilder variableBuilder) {
        putType(astClassFactory.getType(clazz), variableBuilder);
    }

    public void putType(InjectionSignature injectionSignature, InjectionNodeBuilder variableBuilder) {
        this.typeBindings.put(injectionSignature, variableBuilder);
    }

    public void putSignatureMatcher(Matcher<InjectionSignature> matcher, InjectionNodeBuilder variableBuilder) {
        this.typeQualifierBindings.put(matcher, variableBuilder);
    }

    public Map<Matcher<InjectionSignature>, InjectionNodeBuilder> getTypeQualifierBindings() {
        return typeQualifierBindings;
    }

    public Map<InjectionSignature, InjectionNodeBuilder> getTypeBindings() {
        return typeBindings;
    }

    public Set<ASTType> getScopes() {
        return scopeAnnotations.keySet();
    }

    public Map<ASTType, ASTType> getScopeAnnotations(){
        return scopeAnnotations;
    }

    public ScopeAspectFactory getScopeAspectFactory(ASTType scopeType) {
        return scopeVariableBuilderMap.get(scopeType);
    }

    public void putScopeAspectFactory(ASTType scopeAnnotation, ASTType scopeType, ScopeAspectFactory scopeAspectFactory) {
        scopeVariableBuilderMap.put(scopeAnnotation, scopeAspectFactory);
        scopeAnnotations.put(scopeAnnotation, scopeType);
    }

    public Map<ASTType, ScopeAspectFactory> getScopeVariableBuilderMap() {
        return scopeVariableBuilderMap;
    }

    public Map<ASTType, ASTType> getScoping() {
        return scoping;
    }

    public void putScoped(ASTType type, ASTType scope) {
        scoping.put(type, scope);
    }

    public ASTType getScope(ASTType type) {
        return scoping.get(type);
    }

    public void addRepository(InjectionNodeBuilderRepository repository){
        this.typeQualifierBindings.putAll(repository.getTypeQualifierBindings());
        this.typeBindings.putAll(repository.getTypeBindings());
        this.scopeVariableBuilderMap.putAll(repository.getScopeVariableBuilderMap());
        this.scopeAnnotations.putAll(repository.getScopeAnnotations());
        this.scoping.putAll(repository.getScoping());
    }
}
