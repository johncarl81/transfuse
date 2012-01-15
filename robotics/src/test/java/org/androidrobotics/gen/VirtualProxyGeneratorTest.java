package org.androidrobotics.gen;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.SimpleAnalysisContextFactory;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.gen.proxy.DelayedLoad;
import org.androidrobotics.gen.proxy.MockDelegate;
import org.androidrobotics.gen.proxy.MockInterface;
import org.androidrobotics.gen.proxy.VirtualProxyGenerator;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.ProxyDescriptor;
import org.androidrobotics.util.EmptyProcessingEnvironment;
import org.androidrobotics.util.JavaUtilLogger;
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
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this), new EmptyProcessingEnvironment()));
        injector.injectMembers(this);

        ASTType interfaceAST = astClassFactory.buildASTClassType(MockInterface.class);
        ASTType delegateAST = astClassFactory.buildASTClassType(MockDelegate.class);
        delegateInjectionNode = analyzer.analyze(delegateAST, delegateAST, contextFactory.buildContext());

        VirtualProxyAspect proxyAspect = new VirtualProxyAspect();
        proxyAspect.getProxyInterfaces().add(interfaceAST);

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

        assertTrue(delegate.validate(INPUT_VALUE, INPUT_VALUE));
    }

}
