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
package org.androidtransfuse.analysis;

import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.analysis.targets.*;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.gen.CodeGenerationUtil;
import org.androidtransfuse.gen.InjectionFragmentGeneratorHarness;
import org.androidtransfuse.gen.variableBuilder.ProviderVariableBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.PackageClass;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author John Ericksen
 */
@Bootstrap(test = true)
public class LoopAnalysisTest {

    @Inject
    private Analyzer analyzer;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private SimpleAnalysisContextFactory analysisContextFactory;
    private AnalysisContext analysisContext;
    @Inject
    private VariableInjectionBuilder variableInjectionBuilder;
    @Inject
    private InjectionFragmentGeneratorHarness fragmentGeneratorHarness;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    //A -> B (BImpl) -> C -> A
    //D -> E (EImpl) -> F (FProvider.get()) -> D

    @Before
    public void setup() {
        Bootstraps.injectTest(this);

        A.reset();
        BImpl.reset();
        C.reset();

        analysisContext = analysisContextFactory.buildContext();

        analysisContext.getInjectionNodeBuilders().putType(B.class,
                variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(astClassFactory.getType(BImpl.class)));

        analysisContext.getInjectionNodeBuilders().putType(F.class,
                variableInjectionBuilderFactory.buildProviderInjectionNodeBuilder(astClassFactory.getType(FProvider.class)));

        analysisContext.getInjectionNodeBuilders().putType(E.class,
                variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(astClassFactory.getType(EImpl.class)));
    }

    @Test
    public void testProviderLoop() {
        ASTType astType = astClassFactory.getType(D.class);

        InjectionNode injectionNode = analyzer.analyze(astType, astType, analysisContext);
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilder);

        //D -> E
        ConstructorInjectionPoint deConstructorInjectionPoint = injectionNode.getAspect(ASTInjectionAspect.class).getConstructorInjectionPoint();
        assertEquals(1, deConstructorInjectionPoint.getInjectionNodes().size());
        InjectionNode eInjectionNode = deConstructorInjectionPoint.getInjectionNodes().get(0);
        assertTrue(isProxyRequired(eInjectionNode));
        assertEquals(EImpl.class.getCanonicalName(), eInjectionNode.getClassName());

        //E -> F
        ConstructorInjectionPoint efConstructorInjectionPoint = eInjectionNode.getAspect(ASTInjectionAspect.class).getConstructorInjectionPoint();
        assertEquals(1, efConstructorInjectionPoint.getInjectionNodes().size());
        InjectionNode fInjectionNode = efConstructorInjectionPoint.getInjectionNodes().get(0);
        assertFalse(isProxyRequired(fInjectionNode));
        assertEquals(F.class.getCanonicalName(), fInjectionNode.getClassName());

        //F -> D
        InjectionNode fProviderInjectionNode = ((ProviderVariableBuilder) fInjectionNode.getAspect(VariableBuilder.class)).getProviderInjectionNode();
        assertFalse(isProxyRequired(fProviderInjectionNode));
        assertEquals(FProvider.class.getCanonicalName(), fProviderInjectionNode.getClassName());
    }

    @Test
    public void testLoopGeneration() throws JClassAlreadyExistsException, ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
        ASTType astType = astClassFactory.getType(C.class);

        InjectionNode injectionNode = analyzer.analyze(astType, astType, analysisContext);
        PackageClass providerPC = new PackageClass("org.androidtransfuse", "TestProvider_Example1");
        fragmentGeneratorHarness.buildProvider(injectionNode, providerPC);

        ClassLoader classLoader = codeGenerationUtil.build();

        Class<Provider<C>> providerClass = (Class<Provider<C>>) classLoader.loadClass(providerPC.getCanonicalName());

        Provider<C> testProvider = providerClass.newInstance();

        C c = testProvider.get();

        assertEquals(2, BImpl.getConstructionCounter());
        //only one C because we have already created a C in the injection stack parents
        assertEquals(1, C.getConstructionCounter());
        assertEquals(1, A.getConstructionCounter());
        assertEquals(c, c.getA().getB().getC());
    }

    @Test
    public void testLoopRootGeneration() throws JClassAlreadyExistsException, ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
        ASTType astType = astClassFactory.getType(B.class);
        ASTType astImplType = astClassFactory.getType(BImpl.class);

        InjectionNode injectionNode = analyzer.analyze(astType, astImplType, analysisContext);
        PackageClass providerPC = new PackageClass("org.androidtransfuse", "TestProvider_Example2");
        fragmentGeneratorHarness.buildProvider(injectionNode, providerPC);

        ClassLoader classLoader = codeGenerationUtil.build();

        Class<Provider<B>> providerClass = (Class<Provider<B>>) classLoader.loadClass(providerPC.getCanonicalName());

        Provider<B> testProvider = providerClass.newInstance();

        B b = testProvider.get();

        assertEquals(1, BImpl.getConstructionCounter());
        //only one C because we have already created a C in the injection stack parents
        assertEquals(1, C.getConstructionCounter());
        assertEquals(1, A.getConstructionCounter());
        assertEquals(b, b.getC().getA().getB());
    }

    @Test
    public void testLoopWithPostDependenciesGeneration() throws JClassAlreadyExistsException, ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
        ASTType astType = astClassFactory.getType(A.class);

        InjectionNode injectionNode = analyzer.analyze(astType, astType, analysisContext);
        PackageClass providerPC = new PackageClass("org.androidtransfuse", "TestProvider_Example3");
        fragmentGeneratorHarness.buildProvider(injectionNode, providerPC);

        ClassLoader classLoader = codeGenerationUtil.build();

        Class<Provider<A>> providerClass = (Class<Provider<A>>) classLoader.loadClass(providerPC.getCanonicalName());

        Provider<A> testProvider = providerClass.newInstance();

        A a = testProvider.get();

        assertEquals(2, BImpl.getConstructionCounter());
        assertEquals(2, C.getConstructionCounter());
        assertEquals(1, A.getConstructionCounter());
        assertEquals(a, a.getB().getC().getA());

    }

    @Test
    public void testBackLinkAnalysis() {
        ASTType astType = astClassFactory.getType(A.class);

        InjectionNode injectionNode = analyzer.analyze(astType, astType, analysisContext);
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilder);

        //A -> B
        ConstructorInjectionPoint abConstructorInjectionPoint = injectionNode.getAspect(ASTInjectionAspect.class).getConstructorInjectionPoint();
        assertEquals(1, abConstructorInjectionPoint.getInjectionNodes().size());
        InjectionNode bInjectionNode = abConstructorInjectionPoint.getInjectionNodes().get(0);
        assertTrue(isProxyRequired(bInjectionNode));
        assertEquals(BImpl.class.getCanonicalName(), bInjectionNode.getClassName());

        //B -> C
        ConstructorInjectionPoint bcConstructorInjectionPoint = bInjectionNode.getAspect(ASTInjectionAspect.class).getConstructorInjectionPoint();
        assertEquals(1, bcConstructorInjectionPoint.getInjectionNodes().size());
        InjectionNode cInjectionNode = bcConstructorInjectionPoint.getInjectionNodes().get(0);
        assertFalse(isProxyRequired(cInjectionNode));
        assertEquals(C.class.getCanonicalName(), cInjectionNode.getClassName());

        //C -> A
        ConstructorInjectionPoint caConstructorInjectionPoint = cInjectionNode.getAspect(ASTInjectionAspect.class).getConstructorInjectionPoint();
        assertEquals(1, caConstructorInjectionPoint.getInjectionNodes().size());
        InjectionNode aInjectionNode = caConstructorInjectionPoint.getInjectionNodes().get(0);
        assertFalse(isProxyRequired(aInjectionNode));
        assertEquals(A.class.getCanonicalName(), aInjectionNode.getClassName());
        assertEquals(injectionNode, aInjectionNode);
    }

    private boolean isProxyRequired(InjectionNode injectionNode) {
        VirtualProxyAspect proxyAspect = injectionNode.getAspect(VirtualProxyAspect.class);

        return proxyAspect != null && proxyAspect.isProxyRequired();
    }
}
