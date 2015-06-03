/**
 * Copyright 2011-2015 John Ericksen
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
package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.analysis.astAnalyzer.ScopeAspect;
import org.androidtransfuse.model.Aspect;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.scope.Scopes;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;


/**
 * @author John Ericksen
 */
public class ProviderGenerator {

    private static final String SCOPED_EXTENSION = "Provider";
    private static final String UNSCOPED_EXTENSION = "UnscopedProvider";

    private static final String GET_METHOD = "get";

    private final ProviderCache cache;
    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final InstantiationStrategyFactory instantiationStrategyFactory;
    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer variableNamer;
    private final ClassNamer classNamer;

    @Singleton
    public static class ProviderCache {
        private final Map<String, Map<InjectionSignature, JDefinedClass>> providerExtendedClasses = new HashMap<String, Map<InjectionSignature, JDefinedClass>>();

        public synchronized JDefinedClass getCached(InjectionNode injectionNode, ProviderGenerator providerGenerator, String extension) {

            if(!providerExtendedClasses.containsKey(extension)){
                providerExtendedClasses.put(extension, new HashMap<InjectionSignature, JDefinedClass>());
            }
            Map<InjectionSignature, JDefinedClass> providerClasses = providerExtendedClasses.get(extension);

            if (!providerClasses.containsKey(injectionNode.getTypeSignature())) {
                JDefinedClass providerClass = providerGenerator.innerGenerateProvider(injectionNode, extension);
                providerClasses.put(injectionNode.getTypeSignature(), providerClass);
                providerGenerator.fillInProvider(injectionNode, providerClass);
            }

            return providerClasses.get(injectionNode.getTypeSignature());
        }
    }

    @Inject
    public ProviderGenerator(ProviderCache cache, InjectionFragmentGenerator injectionFragmentGenerator, InstantiationStrategyFactory instantiationStrategyFactory, ClassGenerationUtil generationUtil, UniqueVariableNamer variableNamer, ClassNamer classNamer) {
        this.cache = cache;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.instantiationStrategyFactory = instantiationStrategyFactory;
        this.generationUtil = generationUtil;
        this.variableNamer = variableNamer;
        this.classNamer = classNamer;
    }

    public JDefinedClass generateProvider(InjectionNode injectionNode, boolean removeScope) {

        if(removeScope){
            return cache.getCached(unscoped(injectionNode), this, UNSCOPED_EXTENSION);

        }
        else{
            return cache.getCached(injectionNode, this, SCOPED_EXTENSION);
        }
    }

    private InjectionNode unscoped(InjectionNode input){
        InjectionNode nonScopedInjectionNode = new InjectionNode(input.getSignature(), new InjectionSignature(input.getASTType()));

        for (Map.Entry<Class, Aspect> aspectEntry : input.getAspects().entrySet()) {
            nonScopedInjectionNode.addAspect(aspectEntry.getKey(), aspectEntry.getValue());
        }

        nonScopedInjectionNode.getAspects().remove(ScopeAspect.class);
        return nonScopedInjectionNode;
    }

    protected JDefinedClass innerGenerateProvider(InjectionNode injectionNode, String extension) {

        try {
            JClass injectionNodeClassRef = generationUtil.ref(injectionNode.getASTType());

            PackageClass providerClassName = classNamer.numberedClassName(injectionNode.getASTType())
                    .append(extension)
                    .namespaced()
                    .build();

            JDefinedClass providerClass = generationUtil.defineClass(providerClassName);

            providerClass._implements(generationUtil.ref(Provider.class).narrow(injectionNodeClassRef));

            return providerClass;

        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Error while creating provider", e);
        }
    }

    protected JDefinedClass fillInProvider(InjectionNode injectionNode, JDefinedClass providerClass) {

        try{
            JClass injectionNodeClassRef = generationUtil.ref(injectionNode.getASTType());

            //scope holder definition
            JFieldVar scopesField = providerClass.field(JMod.PRIVATE, Scopes.class, variableNamer.generateName(Scopes.class));

            JMethod constructor = providerClass.constructor(JMod.PUBLIC);
            JVar scopesParam = constructor.param(Scopes.class, variableNamer.generateName(Scopes.class));

            constructor.body().assign(scopesField, scopesParam);


            //get() method
            JMethod getMethod = providerClass.method(JMod.PUBLIC, injectionNodeClassRef, GET_METHOD);

            JBlock getMethodBody = getMethod.body();

            Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(getMethodBody,
                    instantiationStrategyFactory.buildMethodStrategy(getMethodBody, scopesField), providerClass, injectionNode, scopesField);

            getMethodBody._return(expressionMap.get(injectionNode).getExpression());

            return providerClass;
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Error while creating provider", e);
        }
    }
}
