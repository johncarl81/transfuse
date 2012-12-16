/**
 * Copyright 2012 John Ericksen
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

    private static final String GET_METHOD = "get";
    private static final String PROVIDER_EXT = "_Provider";

    private final ProviderCache cache;
    private final JCodeModel codeModel;
    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final ClassGenerationUtil generationUtil;

    @Singleton
    public static class ProviderCache {
        private final Map<String, JDefinedClass> providerClasses = new HashMap<String, JDefinedClass>();

        public synchronized JDefinedClass getCached(InjectionNode injectionNode, ProviderGenerator providerGenerator) {
            if (!providerClasses.containsKey(injectionNode.getClassName())) {
                JDefinedClass providerClass = providerGenerator.innerGenerateProvider(injectionNode);
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

    public JDefinedClass generateProvider(InjectionNode injectionNode) {
        return cache.getCached(injectionNode, this);
    }

    protected JDefinedClass innerGenerateProvider(InjectionNode injectionNode) {

        try {
            JClass injectionNodeClassRef = codeModel.ref(injectionNode.getClassName());

            JDefinedClass providerClass = generationUtil.defineClass(injectionNode.getASTType().getPackageClass().append(PROVIDER_EXT));

            providerClass._implements(codeModel.ref(Provider.class).narrow(injectionNodeClassRef));

            //todo:possible context variable injections?
            //get() method
            JMethod getMethod = providerClass.method(JMod.PUBLIC, injectionNodeClassRef, GET_METHOD);

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
