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
package org.androidtransfuse.gen.proxy;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.SimpleAnalysisContextFactory;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.gen.CodeGenerationUtil;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.util.DelayedLoad;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author John Ericksen
 */
@Bootstrap
public class VirtualProxyGeneratorTest {

    public static final String TEST_VALUE = "test";
    private static final String INPUT_VALUE = "input";
    private static final int SECOND_VALUE = 42;

    private InjectionNode delegateInjectionNode;
    @Inject
    private VirtualProxyGenerator virtualProxyGenerator;
    @Inject
    public ASTClassFactory astClassFactory;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private Analyzer analyzer;
    @Inject
    private SimpleAnalysisContextFactory contextFactory;

    @Before
    public void setup() {
        Bootstraps.inject(this);

        ASTType interfaceAST = astClassFactory.getType(MockInterface.class);
        ASTType secondInterfaceAST = astClassFactory.getType(SecondMockInteface.class);
        ASTType delegateAST = astClassFactory.getType(MockDelegate.class);
        delegateInjectionNode = analyzer.analyze(delegateAST, delegateAST, contextFactory.buildContext());

        VirtualProxyAspect proxyAspect = new VirtualProxyAspect();
        proxyAspect.getProxyInterfaces().add(interfaceAST);
        proxyAspect.getProxyInterfaces().add(secondInterfaceAST);

        delegateInjectionNode.addAspect(proxyAspect);
    }

    @Test
    public void testProxyByDelayed() throws JClassAlreadyExistsException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        JClass proxyDescriptor = virtualProxyGenerator.generateProxy(delegateInjectionNode);
        virtualProxyGenerator.generateProxies();

        ClassLoader classLoader = codeGenerationUtil.build();

        Class<?> proxyClass = classLoader.loadClass(proxyDescriptor.fullName());

        MockDelegate delegate = new MockDelegate();

        DelayedLoad<MockDelegate> delayedLoadProxy = (DelayedLoad<MockDelegate>) proxyClass.newInstance();

        delayedLoadProxy.load(delegate);

        runMockDelegateTests((MockInterface) delayedLoadProxy, delegate);
    }

    private void runMockDelegateTests(MockInterface proxy, MockDelegate delegate) {
        proxy.execute();
        assertEquals(TEST_VALUE, proxy.getValue());
        proxy.setValue(INPUT_VALUE);
        assertEquals(TEST_VALUE, proxy.passThroughValue(INPUT_VALUE));
        ((SecondMockInteface) proxy).setValue(SECOND_VALUE);

        assertTrue(delegate.validate(INPUT_VALUE, INPUT_VALUE, SECOND_VALUE));
    }

}
