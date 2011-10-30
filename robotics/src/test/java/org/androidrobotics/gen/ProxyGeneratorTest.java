package org.androidrobotics.gen;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidrobotics.analysis.TypeInjectionAnalyzer;
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
    private ASTType delegateAST;
    private InjectionNode delegateInjectionNode;
    @Inject
    private ProxyGenerator proxyGenerator;

    @Inject
    public ASTClassFactory astClassFactory;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private TypeInjectionAnalyzer typeInjectionAnalyzer;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this)));
        injector.injectMembers(this);

        interfaceAST = astClassFactory.buildASTClassType(MockInterface.class);
        delegateAST = astClassFactory.buildASTClassType(MockDelegate.class);
        delegateInjectionNode = typeInjectionAnalyzer.analyze(delegateAST);
    }

    @Test
    public void testProxy() throws JClassAlreadyExistsException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ProxyDescriptor proxyDescriptor = proxyGenerator.generateProxy(interfaceAST, delegateInjectionNode);

        ClassLoader classLoader = codeGenerationUtil.build();

        Class<?> proxyClass = classLoader.loadClass(proxyDescriptor.getClassDefinition().fullName());

        MockDelegate delegate = new MockDelegate();

        Constructor<?> proxyConstructor = proxyClass.getConstructor(MockDelegate.class);
        MockInterface proxy = (MockInterface) proxyConstructor.newInstance(delegate);

        proxy.execute();
        assertEquals(TEST_VALUE, proxy.getValue());
        proxy.setValue(INPUT_VALUE);
        assertEquals(TEST_VALUE, proxy.passThroughValue(INPUT_VALUE));

        assertTrue(delegate.validate(INPUT_VALUE, INPUT_VALUE));


    }


}
