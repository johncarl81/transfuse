package org.androidtransfuse.gen;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class InjectorGeneratorTest {

    @Inject
    private InjectorGenerator injectorGenerator;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private ASTClassFactory astClassFactory;

    private Injector injector;

    public interface Injector {

        InjectorTarget getInjectorTarget();

        InjectorTarget getTargetWithDependency(InjectorTargetDependency dependency);
    }

    public static class InjectorTarget {

        @Inject
        public InjectorTargetDependency dependency;

        public InjectorTargetDependency getDependency() {
            return dependency;
        }
    }

    public static class InjectorTargetDependency {
    }

    @Before
    public void setUp() throws Exception {
        TransfuseTestInjector.inject(this);

        ASTType injectorType = astClassFactory.getType(Injector.class);

        JDefinedClass injectorDefinedClass = injectorGenerator.generate(injectorType);

        ClassLoader classLoader = codeGenerationUtil.build();

        Class<Injector> injectorClass = (Class<Injector>) classLoader.loadClass(injectorDefinedClass.fullName());

        injector = injectorClass.newInstance();
    }

    @Test
    public void testInjectionTarget() {
        InjectorTarget injectorTarget = injector.getInjectorTarget();
        assertNotNull(injectorTarget);
        assertNotNull(injectorTarget.getDependency());
    }

    @Test
    public void testInjectionTargetDependency() {
        InjectorTargetDependency dependency = new InjectorTargetDependency();

        InjectorTarget injectorTarget = injector.getTargetWithDependency(dependency);
        assertNotNull(injectorTarget);
        assertNotNull(injectorTarget.getDependency());
        assertEquals(dependency, injectorTarget.getDependency());
    }
}
