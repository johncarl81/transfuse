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

import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.SimpleAnalysisContextFactory;
import org.androidtransfuse.analysis.astAnalyzer.AOPProxyAspect;
import org.androidtransfuse.gen.proxy.MockDelegate;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.PackageClass;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static junit.framework.Assert.*;

/**
 * @author John Ericksen
 */
public class AOPProxyGeneratorTest {

    public static final String TEST_VALUE = "test";
    private static final String INPUT_VALUE = "input";
    private static final String EXECUTE_METHOD = "execute";
    private static final String SETVALUE_METHOD = "setValue";
    private static final String PASSTHROUGH_METHOD = "passThroughValue";
    private static final String TEST_PACKLAGE = "org.androidtranfuse.gen";
    private static final String TEST_NAME = "MockDelegate_AOPProxy";
    private static final String PRIMITIVE_METHOD = "primitiveCall";
    private static final int SECOND_VALUE = 42;
    private static final PackageClass TEST_PACKAGE_FILENAME = new PackageClass(TEST_PACKLAGE, TEST_NAME);

    private InjectionNode delegateInjectionNode;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private Analyzer analyzer;
    @Inject
    private SimpleAnalysisContextFactory contextFactory;
    private ASTType delegateAST;
    @Inject
    private InjectionFragmentGeneratorHarness fragmentGeneratorHarness;
    @Inject
    private InjectionPointFactory injectionPointFactory;
    @Inject
    private SimpleAnalysisContextFactory simpleAnalysisContextFactory;
    private ASTType mockMethdInterceptorAST;
    @Inject
    private AOPProxyAspect aopProxyAspect;
    @Inject
    private Provider<VariableInjectionBuilder> variableInjectionBuilderProvider;

    @Before
    public void setup() {
        TransfuseTestInjector.inject(this);

        delegateAST = astClassFactory.getType(MockDelegate.class);
        delegateInjectionNode = analyzer.analyze(delegateAST, delegateAST, contextFactory.buildContext());
        delegateInjectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

        mockMethdInterceptorAST = astClassFactory.getType(MockMethodInterceptor.class);
    }

    private InjectionNode buildMethodInterceptorInjectionNode() {
        return injectionPointFactory.buildInjectionNode(mockMethdInterceptorAST, simpleAnalysisContextFactory.buildContext());
    }

    @Test
    public void testAOPProxy() throws JClassAlreadyExistsException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        aopProxyAspect.addInterceptor(buildASTClassMethod(delegateAST, EXECUTE_METHOD), buildMethodInterceptorInjectionNode());
        delegateInjectionNode.addAspect(AOPProxyAspect.class, aopProxyAspect);

        buildAndTest(delegateInjectionNode);
    }

    @Test
    public void testAOPProxyMethodWithParameters() throws JClassAlreadyExistsException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        aopProxyAspect.addInterceptor(buildASTClassMethod(delegateAST, SETVALUE_METHOD), buildMethodInterceptorInjectionNode());
        delegateInjectionNode.addAspect(AOPProxyAspect.class, aopProxyAspect);

        buildAndTest(delegateInjectionNode);


    }

    @Test
    public void testAOPProxyMethodWithReturn() throws JClassAlreadyExistsException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        aopProxyAspect.addInterceptor(buildASTClassMethod(delegateAST, PASSTHROUGH_METHOD), buildMethodInterceptorInjectionNode());
        delegateInjectionNode.addAspect(AOPProxyAspect.class, aopProxyAspect);

        buildAndTest(delegateInjectionNode);
    }

    @Test
    public void testAOPProxyMethodWithPrimitiveReturn() throws JClassAlreadyExistsException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        aopProxyAspect.addInterceptor(buildASTClassMethod(delegateAST, PRIMITIVE_METHOD), buildMethodInterceptorInjectionNode());
        delegateInjectionNode.addAspect(AOPProxyAspect.class, aopProxyAspect);

        buildAndTest(delegateInjectionNode);
    }

    @Test
    public void testMultiAOPProxyMethod() throws ClassNotFoundException, JClassAlreadyExistsException, IOException, IllegalAccessException, InstantiationException {
        aopProxyAspect.addInterceptor(buildASTClassMethod(delegateAST, PASSTHROUGH_METHOD), buildMethodInterceptorInjectionNode());
        aopProxyAspect.addInterceptor(buildASTClassMethod(delegateAST, PASSTHROUGH_METHOD), buildMethodInterceptorInjectionNode());
        delegateInjectionNode.addAspect(AOPProxyAspect.class, aopProxyAspect);

        buildAndTest(delegateInjectionNode);
    }

    private void buildAndTest(InjectionNode delegateInjectionNode) throws ClassNotFoundException, JClassAlreadyExistsException, IOException, IllegalAccessException, InstantiationException {
        fragmentGeneratorHarness.buildProvider(delegateInjectionNode, TEST_PACKAGE_FILENAME);

        ClassLoader classLoader = codeGenerationUtil.build();
        Class<Provider<MockDelegate>> generatedFactoryClass = (Class<Provider<MockDelegate>>) classLoader.loadClass(TEST_PACKAGE_FILENAME.getCanonicalName());

        assertNotNull(generatedFactoryClass);
        Provider<MockDelegate> provider = generatedFactoryClass.newInstance();

        runMockDelegateTests(provider.get());
    }

    private ASTMethod buildASTClassMethod(ASTType delegateAST, String methodName) {
        for (ASTMethod method : delegateAST.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        assert false : "method not found";
        return null;
    }

    private void runMockDelegateTests(MockDelegate proxy) {
        proxy.execute();
        assertEquals(TEST_VALUE, proxy.getValue());
        proxy.setValue(INPUT_VALUE);
        assertEquals(TEST_VALUE, proxy.passThroughValue(INPUT_VALUE));
        assertTrue(proxy.primitiveCall());
        proxy.setValue(SECOND_VALUE);

        assertTrue(proxy.validate(INPUT_VALUE, INPUT_VALUE, SECOND_VALUE));
    }

}
