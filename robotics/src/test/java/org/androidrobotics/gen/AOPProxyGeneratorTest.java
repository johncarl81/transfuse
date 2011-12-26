package org.androidrobotics.gen;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.SimpleAnalysisContextFactory;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.analysis.astAnalyzer.AOPProxyAspect;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.gen.proxy.AOPProxyGenerator;
import org.androidrobotics.gen.proxy.MockDelegate;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.ProxyDescriptor;
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
public class AOPProxyGeneratorTest {

    public static final String TEST_VALUE = "test";
    private static final String INPUT_VALUE = "input";
    private static final String EXECUTE_METHOD = "execute";

    private InjectionNode delegateInjectionNode;
    @Inject
    private AOPProxyGenerator aopProxyGenerator;
    @Inject
    public ASTClassFactory astClassFactory;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private Analyzer analyzer;
    @Inject
    private SimpleAnalysisContextFactory contextFactory;
    private ASTType delegateAST;
    @Inject
    private MockMethodInterceptor mockMethodInterceptor;
    private ASTType mockMethdInterceptorAST;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this)));
        injector.injectMembers(this);

        delegateAST = astClassFactory.buildASTClassType(MockDelegate.class);
        delegateInjectionNode = analyzer.analyze(delegateAST, delegateAST, contextFactory.buildContext());

        mockMethdInterceptorAST = astClassFactory.buildASTClassType(MockMethodInterceptor.class);
    }

    @Test
    public void testAOPProxy() throws JClassAlreadyExistsException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        AOPProxyAspect aopProxyAspect = new AOPProxyAspect();

        aopProxyAspect.getMethodInterceptors().put(buildASTClassMethod(delegateAST, EXECUTE_METHOD), mockMethdInterceptorAST);
        delegateInjectionNode.addAspect(AOPProxyAspect.class, aopProxyAspect);

        ProxyDescriptor proxyDescriptor = aopProxyGenerator.generateProxy(delegateInjectionNode);

        ClassLoader classLoader = codeGenerationUtil.build(true);

        Class<?> proxyClass = classLoader.loadClass(proxyDescriptor.getClassDefinition().fullName());

        MockDelegate delayedLoadProxy = (MockDelegate) proxyClass.newInstance();

        runMockDelegateTests(delayedLoadProxy);
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

        assertTrue(proxy.validate(INPUT_VALUE, INPUT_VALUE));
    }

}
