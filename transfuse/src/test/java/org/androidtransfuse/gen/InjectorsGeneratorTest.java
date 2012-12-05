package org.androidtransfuse.gen;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class InjectorsGeneratorTest {

    @Inject
    private InjectorsGenerator injectorsGenerator;
    @Inject
    private InjectorGenerator injectorGenerator;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;

    private Class injectorsClass;

    public interface Injector {
    }

    private class SingletonProvider<T> implements Provider<T> {
        T value;

        private SingletonProvider(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }
    }

    @Before
    public void setUp() throws Exception {
        TransfuseTestInjector.inject(this);

        ASTType injectorType = astClassFactory.getType(Injector.class);
        JDefinedClass injectorGeneratedClass = injectorGenerator.generate(injectorType);

        JDefinedClass injectorsDefinedClass = injectorsGenerator.generateInjectors(Collections.<Provider<ASTType>, JDefinedClass>singletonMap(
                new SingletonProvider<ASTType>(injectorType), injectorGeneratedClass));

        ClassLoader classLoader = codeGenerationUtil.build();

        injectorsClass = classLoader.loadClass(injectorsDefinedClass.fullName());
    }

    @Test
    public void test() throws Exception {
        assertNotNull(injectorsClass.getMethod("get", Class.class).invoke(null, Injector.class));
    }
}
