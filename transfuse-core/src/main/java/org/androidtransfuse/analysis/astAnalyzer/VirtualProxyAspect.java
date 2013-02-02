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
package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.model.TypedExpression;

import java.util.HashSet;
import java.util.Set;

/**
 * Aspect to flag the the given InjectionNode requires a virtual proxy.  This is due to some circular dependency
 * which is broken by a virtual proxy.
 *
 * @author John Ericksen
 */
public class VirtualProxyAspect {

    private final Set<ASTType> proxyInterfaces = new HashSet<ASTType>();
    private boolean proxyDefined = false;
    private TypedExpression proxyExpression;

    public Set<ASTType> getProxyInterfaces() {
        return proxyInterfaces;
    }

    public boolean isProxyRequired() {
        return !proxyInterfaces.isEmpty();
    }

    public boolean isProxyDefined() {
        return proxyDefined;
    }

    public void setProxyDefined(boolean proxyDefined) {
        this.proxyDefined = proxyDefined;
    }

    public void setProxyExpression(TypedExpression proxyExpression) {
        this.proxyExpression = proxyExpression;
    }

    public TypedExpression getProxyExpression() {
        return proxyExpression;
    }
}
