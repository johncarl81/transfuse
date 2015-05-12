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
package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.MethodSignature;
import org.androidtransfuse.model.Aspect;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.InjectionNodeLogger;

import java.util.*;

/**
 * Represents the AOP proxy Method Interceptors.  This aspect is applied to the InjectionNode and used during the
 * object instantiation generation.
 *
 * @author John Ericksen
 */
public class AOPProxyAspect implements Aspect {

    private final Map<MethodSignature, Set<InjectionNode>> methodInterceptors = new HashMap<MethodSignature, Set<InjectionNode>>();
    private final Map<MethodSignature, ASTMethod> methodMapping = new HashMap<MethodSignature, ASTMethod>();

    public Map<ASTMethod, Set<InjectionNode>> getMethodInterceptors() {
        Map<ASTMethod, Set<InjectionNode>> unboxedMethodInterceptors = new HashMap<ASTMethod, Set<InjectionNode>>();

        for (Map.Entry<MethodSignature, Set<InjectionNode>> methodSignatureSetEntry : methodInterceptors.entrySet()) {
            ASTMethod astMethod = methodMapping.get(methodSignatureSetEntry.getKey());
            unboxedMethodInterceptors.put(astMethod, methodSignatureSetEntry.getValue());
        }

        return unboxedMethodInterceptors;
    }

    public void addInterceptors(Collection<ASTMethod> methods, InjectionNode interceptorInjectionNode) {
        for (ASTMethod method : methods) {
            addInterceptor(method, interceptorInjectionNode);
        }
    }

    public void addInterceptor(ASTMethod astMethod, InjectionNode interceptorInjectionNode) {
        MethodSignature methodSignature = new MethodSignature(astMethod);
        if (!methodInterceptors.containsKey(methodSignature)) {
            methodInterceptors.put(methodSignature, new HashSet<InjectionNode>());
        }

        methodMapping.put(methodSignature, astMethod);
        methodInterceptors.get(methodSignature).add(interceptorInjectionNode);
    }

    @Override
    public void log(InjectionNodeLogger logger) {
        logger.append("AOPProxyAspect");
    }
}
