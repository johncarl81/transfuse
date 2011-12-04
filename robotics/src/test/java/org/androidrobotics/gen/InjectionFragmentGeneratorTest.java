package org.androidrobotics.gen;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidrobotics.analysis.AnalysisDependencyProcessingCallback;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.gen.target.*;
import org.androidrobotics.model.*;
import org.androidrobotics.util.JavaUtilLogger;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;

import static junit.framework.Assert.*;

/**
 * @author John Ericksen
 */
public class InjectionFragmentGeneratorTest {

    @Inject
    private InjectionFragmentGeneratorHarness fragmentGeneratorHarness;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private JCodeModel codeModel;
    private VariableBuilderRepository variableBuilderRepository;
    @Inject
    private ProviderVariableBuilderFactory providerVariableBuilderFactory;
    @Inject
    private ASTClassFactory astClassFactory;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this)));
        injector.injectMembers(this);

        VariableBuilderRepositoryFactory variableBuilderRepositoryFactory = injector.getInstance(VariableBuilderRepositoryFactory.class);

        variableBuilderRepository = variableBuilderRepositoryFactory.buildRepository();
    }

    @Test
    public void testConstrictorInjection() throws Exception {
        InjectionNode injectionNode = new InjectionNode(astClassFactory.buildASTClassType(ConstructorInjectable.class));
        //setup constructor injection
        ConstructorInjectionPoint constructorInjectionPoint = new ConstructorInjectionPoint();
        constructorInjectionPoint.addInjectionNode(buildInjectionNode(InjectionTarget.class));
        injectionNode.addInjectionPoint(constructorInjectionPoint);

        ConstructorInjectable constructorInjectable = buildInstance(ConstructorInjectable.class, injectionNode);

        assertNotNull(constructorInjectable.getInjectionTarget());
    }

    @Test
    public void testMethodInjection() throws Exception {
        InjectionNode injectionNode = buildInjectionNode(MethodInjectable.class);

        MethodInjectionPoint methodInjectionPoint = new MethodInjectionPoint("setInjectionTarget");
        methodInjectionPoint.addInjectionNode(buildInjectionNode(InjectionTarget.class));
        injectionNode.addInjectionPoint(methodInjectionPoint);

        MethodInjectable methodInjectable = buildInstance(MethodInjectable.class, injectionNode);

        assertNotNull(methodInjectable.getInjectionTarget());
    }

    @Test
    public void testFieldInjection() throws Exception {
        InjectionNode injectionNode = buildInjectionNode(FieldInjectable.class);

        FieldInjectionPoint fieldInjectionPoint = new FieldInjectionPoint("injectionTarget", buildInjectionNode(InjectionTarget.class));
        injectionNode.addInjectionPoint(fieldInjectionPoint);

        FieldInjectable fieldInjectable = buildInstance(FieldInjectable.class, injectionNode);

        assertNotNull(fieldInjectable.getInjectionTarget());
    }

    @Test
    public void testDelayedProxyInjection() throws Exception {
        InjectionNode injectionNode = new InjectionNode(astClassFactory.buildASTClassType(DelayedProxyTarget.class));

        injectionNode.setProxyRequired(true);
        injectionNode.addProxyInterface(astClassFactory.buildASTClassType(DelayedProxy.class));

        //setup constructor injection
        ConstructorInjectionPoint constructorInjectionPoint = new ConstructorInjectionPoint();
        InjectionNode dependencyInjectionNode = new InjectionNode(astClassFactory.buildASTClassType(DelayedProxyDependency.class));
        constructorInjectionPoint.addInjectionNode(dependencyInjectionNode);
        injectionNode.addInjectionPoint(constructorInjectionPoint);

        //reference circle
        ConstructorInjectionPoint dependencyConstructorInjectionPoint = new ConstructorInjectionPoint();
        dependencyConstructorInjectionPoint.addInjectionNode(injectionNode);
        dependencyInjectionNode.addInjectionPoint(dependencyConstructorInjectionPoint);

        DelayedProxyTarget proxyTarget = buildInstance(DelayedProxyTarget.class, injectionNode);

        assertNotNull(proxyTarget.getDelayedProxyDependency());
        assertNotNull(proxyTarget.getDelayedProxyDependency().getDelayedProxy());
        //assertEquals(proxyTarget, proxyTarget.getDelayedProxyDependency().getDelayedProxy());
        assertTrue(proxyTarget.getDelayedProxyDependency().getDelayedProxy() instanceof DelayedProxy);
    }

    @Test
    public void testVariableBuilder() throws Exception {
        InjectionNode injectionNode = buildInjectionNode(VariableBuilderInjectable.class);

        FieldInjectionPoint fieldInjectionPoint = new FieldInjectionPoint("target", buildInjectionNode(VariableTarget.class));
        injectionNode.addInjectionPoint(fieldInjectionPoint);

        variableBuilderRepository.put(VariableTarget.class.getName(), new VariableBuilder() {
            @Override
            public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext) {
                return JExpr._new(codeModel.ref(VariableTarget.class));
            }

            @Override
            public InjectionNode processInjectionNode(ASTType astType, AnalysisDependencyProcessingCallback callback) {
                return callback.processInjectionNode(astType);
            }
        });

        VariableBuilderInjectable vbInjectable = buildInstance(VariableBuilderInjectable.class, injectionNode);

        assertNotNull(vbInjectable.getTarget());
    }

    @Test
    public void testProviderBuilder() throws Exception {

        variableBuilderRepository.put(VariableTarget.class.getName(), providerVariableBuilderFactory.buildProviderVariableBuilder(VariableTargetProvider.class));

        InjectionNode injectionNode = buildInjectionNode(VariableTarget.class);
        injectionNode.putBuilderResource(ProviderVariableBuilder.PROVIDER_INJECTION_NODE, buildInjectionNode(VariableTargetProvider.class));

        VariableTarget target = buildInstance(VariableTarget.class, injectionNode);

        assertNotNull(target);
    }

    private InjectionNode buildInjectionNode(Class<?> instanceClass) {
        InjectionNode injectionNode = new InjectionNode(astClassFactory.buildASTClassType(instanceClass));

        ConstructorInjectionPoint noArgConstructorInjectionPoint = new ConstructorInjectionPoint();
        injectionNode.addInjectionPoint(noArgConstructorInjectionPoint);

        return injectionNode;
    }

    private <T> T buildInstance(Class<T> instanceClass, InjectionNode injectionNode) throws Exception {
        PackageClass providerPackageClass = new PackageClass(instanceClass).add("Provider");

        fragmentGeneratorHarness.buildProvider(injectionNode, providerPackageClass, variableBuilderRepository);

        ClassLoader classLoader = codeGenerationUtil.build(true);
        Class<Provider> generatedFactoryClass = (Class<Provider>) classLoader.loadClass(providerPackageClass.getFullyQualifiedName());

        assertNotNull(generatedFactoryClass);
        Provider provider = generatedFactoryClass.newInstance();
        Object result = provider.get();
        assertEquals(instanceClass, result.getClass());

        return (T) result;
    }
}
