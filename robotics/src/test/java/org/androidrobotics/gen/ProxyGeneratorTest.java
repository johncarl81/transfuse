package org.androidrobotics.gen;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.gen.proxy.MockDelegate;
import org.androidrobotics.gen.proxy.MockInterface;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.ProxyDescriptor;
import org.androidrobotics.util.JavaUtilLogger;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author John Ericksen
 */
public class ProxyGeneratorTest {

    public static final String TEST_VALUE = "test";
    private static final String INPUT_VALUE = "input";

    private ASTType interfaceAST;
    private InjectionNode delegateInjectionNode;
    @Inject
    private ProxyGenerator proxyGenerator;
    @Inject
    public ASTClassFactory astClassFactory;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private Analyzer analyzer;
    @Inject
    private DelegateInstantiationGeneratorStrategyFactory delegateInstantiationGeneratorFactory;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this)));
        injector.injectMembers(this);

        interfaceAST = astClassFactory.buildASTClassType(MockInterface.class);
        ASTType delegateAST = astClassFactory.buildASTClassType(MockDelegate.class);
        delegateInjectionNode = analyzer.analyze(delegateAST, delegateAST, new AnalysisContext());

        delegateInjectionNode.addProxyInterface(interfaceAST);
    }

    @Test
    public void testProxyByConstructor() throws JClassAlreadyExistsException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        ProxyDescriptor proxyDescriptor = proxyGenerator.generateProxy(delegateInjectionNode, delegateInstantiationGeneratorFactory.buildConstructorStrategy(delegateInjectionNode));

        ClassLoader classLoader = codeGenerationUtil.build(false);

        Class<?> proxyClass = classLoader.loadClass(proxyDescriptor.getClassDefinition().fullName());

        MockDelegate delegate = new MockDelegate();

        Constructor<?> proxyConstructor = proxyClass.getConstructor(MockDelegate.class);

        runMockDelegateTests((MockInterface) proxyConstructor.newInstance(delegate), delegate);
    }

    @Test
    public void testProxyByDelayed() throws JClassAlreadyExistsException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ProxyDescriptor proxyDescriptor = proxyGenerator.generateProxy(delegateInjectionNode, delegateInstantiationGeneratorFactory.buildDelayedStrategy(delegateInjectionNode));

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

        assertTrue(delegate.validate(INPUT_VALUE, INPUT_VALUE));
    }

}
