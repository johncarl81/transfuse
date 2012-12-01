package org.androidtransfuse.module;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.SimpleAnalysisContextFactory;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.targets.MockAnalysisClass;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;
import org.androidtransfuse.util.TransfuseInjectionException;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class InjectionPointFactoryTest {

    private AnalysisContext emptyContext;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private SimpleAnalysisContextFactory contextFactory;
    @Inject
    private InjectionPointFactory injectionPointFactory;

    @Before
    public void setUp() {
        TransfuseTestInjector.inject(this);
        emptyContext = contextFactory.buildContext();
    }

    @Test
    public void testConstructorInjectionPointBuild() {
        Constructor<?>[] constructors = MockAnalysisClass.class.getConstructors();
        Constructor constructor = constructors[0];

        ConstructorInjectionPoint constructorInjectionPoint = injectionPointFactory.buildInjectionPoint(astClassFactory.getType(MockAnalysisClass.class), astClassFactory.getConstructor(constructor), emptyContext);

        TypeVariable[] typeParameters = constructor.getTypeParameters();
        List<InjectionNode> injectionNodes = constructorInjectionPoint.getInjectionNodes();
        for (int i = 0; i < typeParameters.length; i++) {

            InjectionNode injectionNode = injectionNodes.get(i);
            TypeVariable typeParameter = typeParameters[i];
            assertEquals(typeParameter.getName(), injectionNode.getClassName());
        }

        assertEquals(1, constructorInjectionPoint.getThrowsTypes().size());
        assertEquals(astClassFactory.getType(TransfuseInjectionException.class), constructorInjectionPoint.getThrowsTypes().get(0));
    }

    @Test
    public void testMethodInjectionPointBuild() {
        Method[] methods = MockAnalysisClass.class.getDeclaredMethods();
        Method method = methods[0];

        List<ASTParameter> astParameters = astClassFactory.getParameters(method);
        ASTType containingType = astClassFactory.getType(MockAnalysisClass.class);
        ASTMethod astMethod = astClassFactory.getMethod(method);

        MethodInjectionPoint methodInjectionPoint = injectionPointFactory.buildInjectionPoint(containingType, astMethod, emptyContext);


        List<InjectionNode> injectionNodes = methodInjectionPoint.getInjectionNodes();
        for (int i = 0; i < astParameters.size(); i++) {

            InjectionNode injectionNode = injectionNodes.get(i);
            ASTParameter typeParameter = astParameters.get(i);
            assertEquals(typeParameter.getName(), injectionNode.getClassName());
        }

        assertEquals(1, methodInjectionPoint.getThrowsTypes().size());
        assertEquals(astClassFactory.getType(TransfuseInjectionException.class), methodInjectionPoint.getThrowsTypes().get(0));
    }

    @Test
    public void testParameterInjectionPointBuild() {
        Field[] fields = MockAnalysisClass.class.getDeclaredFields();
        Field field = fields[0];

        ASTType containingType = astClassFactory.getType(MockAnalysisClass.class);

        FieldInjectionPoint fieldInjectionPoint = injectionPointFactory.buildInjectionPoint(containingType, astClassFactory.getField(field), emptyContext);

        InjectionNode injectionNode = fieldInjectionPoint.getInjectionNode();

        assertEquals(field.getType().getName(), injectionNode.getClassName());
    }
}
