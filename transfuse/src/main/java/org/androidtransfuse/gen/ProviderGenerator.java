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
package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.astAnalyzer.ScopeAspect;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;


/**
 * @author John Ericksen
 */
public class ProviderGenerator {

    private static final String SCOPED_EXTENSION = "_Provider";
    private static final String UNSCOPED_EXTENSION = "_UnscopedProvider";

    private static final String GET_METHOD = "get";

    private final ProviderCache cache;
    private final JCodeModel codeModel;
    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final ClassGenerationUtil generationUtil;

    @Singleton
    public static class ProviderCache {
        private final Map<String, Map<String, JDefinedClass>> providerExtendedClasses = new HashMap<String, Map<String, JDefinedClass>>();

        public synchronized JDefinedClass getCached(InjectionNode injectionNode, ProviderGenerator providerGenerator, String extension) {

            if(!providerExtendedClasses.containsKey(extension)){
                providerExtendedClasses.put(extension, new HashMap<String, JDefinedClass>());
            }
            Map<String, JDefinedClass> providerClasses = providerExtendedClasses.get(extension);

            if (!providerClasses.containsKey(injectionNode.getClassName())) {
                JDefinedClass providerClass = providerGenerator.innerGenerateProvider(injectionNode, extension);
                providerClasses.put(injectionNode.getClassName(), providerClass);
            }

            return providerClasses.get(injectionNode.getClassName());
        }
    }

    @Inject
    public ProviderGenerator(ProviderCache cache, JCodeModel codeModel, InjectionFragmentGenerator injectionFragmentGenerator, ClassGenerationUtil generationUtil) {
        this.cache = cache;
        this.codeModel = codeModel;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.generationUtil = generationUtil;
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
        InjectionNode nonScopedInjectionNode = new InjectionNode(input.getUsageType(), input.getASTType());

        for (Map.Entry<Class, Object> aspectEntry : input.getAspects().entrySet()) {
            nonScopedInjectionNode.addAspect(aspectEntry.getKey(), aspectEntry.getValue());
        }

        nonScopedInjectionNode.getAspects().remove(ScopeAspect.class);
        return nonScopedInjectionNode;
    }

    protected JDefinedClass innerGenerateProvider(InjectionNode injectionNode, String extension) {

        try {
            JClass injectionNodeClassRef = codeModel.ref(injectionNode.getClassName());

            JDefinedClass providerClass = generationUtil.defineClass(injectionNode.getASTType().getPackageClass().append(extension));

            providerClass._implements(codeModel.ref(Provider.class).narrow(injectionNodeClassRef));

            //get() method
            JMethod getMethod = providerClass.method(JMod.PUBLIC, injectionNodeClassRef, GET_METHOD);
            getMethod.annotate(Override.class);

            JBlock getMethodBody = getMethod.body();

            Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(getMethodBody, providerClass, injectionNode);

            getMethodBody._return(expressionMap.get(injectionNode).getExpression());

            return providerClass;

        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Error while creating provider", e);
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Error while creating provider", e);
        }
    }
}
