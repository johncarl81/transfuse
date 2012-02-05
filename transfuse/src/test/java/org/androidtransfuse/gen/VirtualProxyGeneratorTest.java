package org.androidtransfuse.gen;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.SimpleAnalysisContextFactory;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.config.TransfuseGenerationGuiceModule;
import org.androidtransfuse.gen.proxy.*;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.ProxyDescriptor;
import org.androidtransfuse.util.JavaUtilLogger;
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
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new TransfuseGenerationGuiceModule(new JavaUtilLogger(this)));
        injector.injectMembers(this);

        ASTType interfaceAST = astClassFactory.buildASTClassType(MockInterface.class);
        ASTType secondInterfaceAST = astClassFactory.buildASTClassType(SecondMockInteface.class);
        ASTType delegateAST = astClassFactory.buildASTClassType(MockDelegate.class);
        delegateInjectionNode = analyzer.analyze(delegateAST, delegateAST, contextFactory.buildContext());

        VirtualProxyAspect proxyAspect = new VirtualProxyAspect();
        proxyAspect.getProxyInterfaces().add(interfaceAST);
        proxyAspect.getProxyInterfaces().add(secondInterfaceAST);

        delegateInjectionNode.addAspect(proxyAspect);
    }

    @Test
    public void testProxyByDelayed() throws JClassAlreadyExistsException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ProxyDescriptor proxyDescriptor = virtualProxyGenerator.generateProxy(delegateInjectionNode);

        ClassLoader classLoader = codeGenerationUtil.build(false);

        Class<?> proxyClass = classLoader.loadClass(proxyDescriptor.getClassDefinition().fullName());

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
