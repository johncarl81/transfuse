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
import org.androidtransfuse.gen.proxy.VirtualProxyGenerator;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.scope.Scopes;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;


/**
 * @author John Ericksen
 */
public class InjectionFragmentGeneratorHarness {

    @Inject
    private JCodeModel codeModel;
    @Inject
    private InjectionFragmentGenerator injectionFragmentGenerator;
    @Inject
    private ClassGenerationUtil generationUtil;
    @Inject
    private UniqueVariableNamer namer;
    @Inject
    private VirtualProxyGenerator virtualProxyGenerator;

    public void buildProvider(InjectionNode injectionNode, PackageClass providerPackageClass) throws JClassAlreadyExistsException, ClassNotFoundException {
        JDefinedClass definedClass = generationUtil.defineClass(providerPackageClass);

        JType providedType = codeModel.parseType(injectionNode.getSignature().getType().getName());

        definedClass._implements(codeModel.ref(Provider.class).narrow(providedType));

        JMethod getMethod = definedClass.method(JMod.PUBLIC, providedType, "get");
        getMethod.annotate(Override.class);

        JBlock block = getMethod.body();
        //Singleton scopes holder

        JClass scopesRef = codeModel.ref(Scopes.class);
        JVar scopes = block.decl(scopesRef, namer.generateName(Scopes.class));

        Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, injectionNode, scopes);
        virtualProxyGenerator.generateProxies();

        block._return(expressionMap.get(injectionNode).getExpression());
    }
}
