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

import org.androidtransfuse.adapter.*;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.SimpleAnalysisContextFactory;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.gen.target.*;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.model.*;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@Bootstrap
public class InjectionFragmentGeneratorTest {

    @Inject
    private InjectionFragmentGeneratorHarness fragmentGeneratorHarness;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private Provider<VariableInjectionBuilder> variableInjectionBuilderProvider;
    @Inject
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private Analyzer analyzer;
    @Inject
    private SimpleAnalysisContextFactory contextFactory;
    private AnalysisContext context;
    @Inject
    private InjectionNodeBuilderRepository injectionNodeBuilderRepository;

    @Before
    public void setUp() {
        Bootstraps.inject(this);

        context = contextFactory.buildContext();
    }

    @Test
    public void testConstrictorInjection() throws Exception {
        ASTType constructorInjectableType = astClassFactory.getType(ConstructorInjectable.class);
        ASTConstructor constructor = constructorInjectableType.getConstructors().iterator().next();
        InjectionNode injectionNode = new InjectionNode(new InjectionSignature(constructorInjectableType));
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());
        //setup constructor injection
        ConstructorInjectionPoint constructorInjectionPoint = new ConstructorInjectionPoint(constructorInjectableType, constructor);
        constructorInjectionPoint.addInjectionNode(buildInjectionNode(InjectionTarget.class));
        getInjectionAspect(injectionNode).set(constructorInjectionPoint);

        ConstructorInjectable constructorInjectable = buildInstance(ConstructorInjectable.class, injectionNode);

        assertNotNull(constructorInjectable.getInjectionTarget());
    }

    @Test
    public void testMethodInjection() throws Exception {
        InjectionNode injectionNode = buildInjectionNode(MethodInjectable.class);

        ASTType containingType = astClassFactory.getType(MethodInjectable.class);
        ASTMethod method = getMethod("setInjectionTarget", containingType);

        MethodInjectionPoint methodInjectionPoint = new MethodInjectionPoint(containingType, containingType, method);
        methodInjectionPoint.addInjectionNode(buildInjectionNode(InjectionTarget.class));

        getInjectionAspect(injectionNode).addGroup().add(methodInjectionPoint);

        MethodInjectable methodInjectable = buildInstance(MethodInjectable.class, injectionNode);

        assertNotNull(methodInjectable.getInjectionTarget());
    }

    @Test
    public void testFieldInjection() throws Exception {
        InjectionNode injectionNode = buildInjectionNode(FieldInjectable.class);

        ASTType containingType = astClassFactory.getType(FieldInjectable.class);
        ASTField field = getField("injectionTarget", containingType);

        FieldInjectionPoint fieldInjectionPoint = new FieldInjectionPoint(containingType, containingType, field, buildInjectionNode(InjectionTarget.class));
        getInjectionAspect(injectionNode).addGroup().add(fieldInjectionPoint);

        FieldInjectable fieldInjectable = buildInstance(FieldInjectable.class, injectionNode);

        assertNotNull(fieldInjectable.getInjectionTarget());
    }

    @Test
    public void testDelayedProxyInjection() throws Exception {

        ASTType delayedProxyType = astClassFactory.getType(DelayedProxyDependency.class);
        ASTConstructor delayedProxyConstructor = delayedProxyType.getConstructors().iterator().next();

        InjectionNode injectionNode = new InjectionNode(new InjectionSignature(astClassFactory.getType(DelayedProxy.class)),
                new InjectionSignature(astClassFactory.getType(DelayedProxyTarget.class)));
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

        VirtualProxyAspect proxyAspect = new VirtualProxyAspect();
        proxyAspect.getProxyInterfaces().add(astClassFactory.getType(DelayedProxy.class));

        injectionNode.addAspect(proxyAspect);

        //setup constructor injection
        ConstructorInjectionPoint constructorInjectionPoint = new ConstructorInjectionPoint(delayedProxyType, delayedProxyConstructor);
        InjectionNode dependencyInjectionNode = new InjectionNode(new InjectionSignature(delayedProxyType));
        dependencyInjectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());
        constructorInjectionPoint.addInjectionNode(dependencyInjectionNode);
        getInjectionAspect(injectionNode).set(constructorInjectionPoint);

        //reference circle
        ConstructorInjectionPoint dependencyConstructorInjectionPoint = new ConstructorInjectionPoint(delayedProxyType, delayedProxyConstructor);
        dependencyConstructorInjectionPoint.addInjectionNode(injectionNode);
        getInjectionAspect(dependencyInjectionNode).set(dependencyConstructorInjectionPoint);

        DelayedProxyTarget proxyTarget = buildInstance(DelayedProxyTarget.class, injectionNode);

        assertNotNull(proxyTarget.getDelayedProxyDependency());
        assertNotNull(proxyTarget.getDelayedProxyDependency().getDelayedProxy());
    }

    @Test
    public void testVariableBuilder() throws Exception {
        InjectionNode injectionNode = buildInjectionNode(VariableBuilderInjectable.class);

        ASTType containingType = astClassFactory.getType(VariableBuilderInjectable.class);
        ASTField field = getField("target", containingType);

        FieldInjectionPoint fieldInjectionPoint = new FieldInjectionPoint(containingType, containingType, field, buildInjectionNode(VariableTarget.class));
        getInjectionAspect(injectionNode).addGroup().add(fieldInjectionPoint);

        injectionNodeBuilderRepository.putType(VariableTarget.class, new InjectionNodeBuilder() {

            @Override
            public InjectionNode buildInjectionNode(InjectionSignature signature, AnalysisContext context) {
                return analyzer.analyze(signature, context);
            }
        });

        VariableBuilderInjectable vbInjectable = buildInstance(VariableBuilderInjectable.class, injectionNode);

        assertNotNull(vbInjectable.getTarget());
    }

    @Test
    public void testProviderBuilder() throws Exception {

        injectionNodeBuilderRepository.putType(VariableTarget.class, variableInjectionBuilderFactory.buildProviderInjectionNodeBuilder(
                astClassFactory.getType(VariableTargetProvider.class)));

        InjectionNode injectionNode = buildInjectionNode(VariableTarget.class);
        ASTType providerType = astClassFactory.getType(VariableTargetProvider.class);
        InjectionNode providerInjectionNode = analyzer.analyze(providerType, providerType, context);
        providerInjectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildProviderVariableBuilder(providerInjectionNode));

        VariableTarget target = buildInstance(VariableTarget.class, injectionNode);

        assertNotNull(target);
    }

    private InjectionNode buildInjectionNode(Class<?> instanceClass) {
        InjectionNode injectionNode = new InjectionNode(new InjectionSignature(astClassFactory.getType(instanceClass)));
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());
        ASTConstructor constructor = injectionNode.getASTType().getConstructors().iterator().next();

        ConstructorInjectionPoint noArgConstructorInjectionPoint = new ConstructorInjectionPoint(injectionNode.getASTType(), constructor);
        getInjectionAspect(injectionNode).set(noArgConstructorInjectionPoint);

        return injectionNode;
    }

    private <T> T buildInstance(Class<T> instanceClass, InjectionNode injectionNode) throws Exception {
        PackageClass providerPackageClass = new PackageClass(instanceClass).append("Provider");

        fragmentGeneratorHarness.buildProvider(injectionNode, providerPackageClass);

        ClassLoader classLoader = codeGenerationUtil.build();
        Class<Provider> generatedFactoryClass = (Class<Provider>) classLoader.loadClass(providerPackageClass.getCanonicalName());

        assertNotNull(generatedFactoryClass);
        Provider provider = generatedFactoryClass.newInstance();
        Object result = provider.get();
        assertEquals(instanceClass, result.getClass());

        return (T) result;
    }

    private ASTInjectionAspect getInjectionAspect(InjectionNode injectionNode) {
        if (!injectionNode.containsAspect(ASTInjectionAspect.class)) {
            injectionNode.addAspect(new ASTInjectionAspect());
        }
        return injectionNode.getAspect(ASTInjectionAspect.class);
    }

    private ASTMethod getMethod(String name, ASTType containingType) {
        for (ASTMethod astMethod : containingType.getMethods()) {
            if(astMethod.getName().equals(name)){
                return astMethod;
            }
        }
        return null;
    }

    private ASTField getField(String name, ASTType containingType) {
        for (ASTField astField : containingType.getFields()) {
            if(astField.getName().equals(name)){
                return astField;
            }
        }
        return null;
    }
}
