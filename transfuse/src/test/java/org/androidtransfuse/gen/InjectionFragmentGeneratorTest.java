package org.androidtransfuse.gen;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.SimpleAnalysisContextFactory;
import org.androidtransfuse.analysis.adapter.ASTAccessModifier;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.target.*;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilderNoAnnotationAdapter;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.model.*;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class InjectionFragmentGeneratorTest {

    @Inject
    private InjectionFragmentGeneratorHarness fragmentGeneratorHarness;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private Provider<VariableInjectionBuilder> variableInjectionBuilderProvider;
    @Inject
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private Analyzer analyzer;
    @Inject
    private SimpleAnalysisContextFactory contextFactory;
    private AnalysisContext context;
    @Inject
    private InjectionNodeBuilderRepository injectionNodeBuilderRepository;

    @Before
    public void setUp() {
        TransfuseTestInjector.inject(this);

        context = contextFactory.buildContext();
    }

    @Test
    public void testConstrictorInjection() throws Exception {
        InjectionNode injectionNode = new InjectionNode(astClassFactory.buildASTClassType(ConstructorInjectable.class));
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());
        //setup constructor injection
        ConstructorInjectionPoint constructorInjectionPoint = new ConstructorInjectionPoint(ASTAccessModifier.PUBLIC, astClassFactory.buildASTClassType(ConstructorInjectable.class));
        constructorInjectionPoint.addInjectionNode(buildInjectionNode(InjectionTarget.class));
        getInjectionAspect(injectionNode).add(constructorInjectionPoint);

        ConstructorInjectable constructorInjectable = buildInstance(ConstructorInjectable.class, injectionNode);

        assertNotNull(constructorInjectable.getInjectionTarget());
    }

    @Test
    public void testMethodInjection() throws Exception {
        InjectionNode injectionNode = buildInjectionNode(MethodInjectable.class);

        ASTType containingType = astClassFactory.buildASTClassType(MethodInjectable.class);

        MethodInjectionPoint methodInjectionPoint = new MethodInjectionPoint(containingType, ASTAccessModifier.PUBLIC, "setInjectionTarget");
        methodInjectionPoint.addInjectionNode(buildInjectionNode(InjectionTarget.class));
        getInjectionAspect(injectionNode).add(methodInjectionPoint);

        MethodInjectable methodInjectable = buildInstance(MethodInjectable.class, injectionNode);

        assertNotNull(methodInjectable.getInjectionTarget());
    }

    @Test
    public void testFieldInjection() throws Exception {
        InjectionNode injectionNode = buildInjectionNode(FieldInjectable.class);

        ASTType containingType = astClassFactory.buildASTClassType(FieldInjectable.class);

        FieldInjectionPoint fieldInjectionPoint = new FieldInjectionPoint(containingType, ASTAccessModifier.PRIVATE, "injectionTarget", buildInjectionNode(InjectionTarget.class));
        getInjectionAspect(injectionNode).add(fieldInjectionPoint);

        FieldInjectable fieldInjectable = buildInstance(FieldInjectable.class, injectionNode);

        assertNotNull(fieldInjectable.getInjectionTarget());
    }

    @Test
    public void testDelayedProxyInjection() throws Exception {
        InjectionNode injectionNode = new InjectionNode(astClassFactory.buildASTClassType(DelayedProxy.class),
                astClassFactory.buildASTClassType(DelayedProxyTarget.class));
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

        VirtualProxyAspect proxyAspect = new VirtualProxyAspect();
        proxyAspect.getProxyInterfaces().add(astClassFactory.buildASTClassType(DelayedProxy.class));

        injectionNode.addAspect(proxyAspect);

        //setup constructor injection
        ConstructorInjectionPoint constructorInjectionPoint = new ConstructorInjectionPoint(ASTAccessModifier.PUBLIC, astClassFactory.buildASTClassType(DelayedProxyDependency.class));
        InjectionNode dependencyInjectionNode = new InjectionNode(astClassFactory.buildASTClassType(DelayedProxyDependency.class));
        dependencyInjectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());
        constructorInjectionPoint.addInjectionNode(dependencyInjectionNode);
        getInjectionAspect(injectionNode).add(constructorInjectionPoint);

        //reference circle
        ConstructorInjectionPoint dependencyConstructorInjectionPoint = new ConstructorInjectionPoint(ASTAccessModifier.PUBLIC, astClassFactory.buildASTClassType(DelayedProxyDependency.class));
        dependencyConstructorInjectionPoint.addInjectionNode(injectionNode);
        getInjectionAspect(dependencyInjectionNode).add(dependencyConstructorInjectionPoint);

        DelayedProxyTarget proxyTarget = buildInstance(DelayedProxyTarget.class, injectionNode);

        assertNotNull(proxyTarget.getDelayedProxyDependency());
        assertNotNull(proxyTarget.getDelayedProxyDependency().getDelayedProxy());
    }

    @Test
    public void testVariableBuilder() throws Exception {
        InjectionNode injectionNode = buildInjectionNode(VariableBuilderInjectable.class);

        ASTType containingType = astClassFactory.buildASTClassType(VariableBuilderInjectable.class);

        FieldInjectionPoint fieldInjectionPoint = new FieldInjectionPoint(containingType, ASTAccessModifier.PRIVATE, "target", buildInjectionNode(VariableTarget.class));
        getInjectionAspect(injectionNode).add(fieldInjectionPoint);

        injectionNodeBuilderRepository.putType(VariableTarget.class, new InjectionNodeBuilderNoAnnotationAdapter() {

            @Override
            public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
                return analyzer.analyze(astType, astType, context);
            }
        });

        VariableBuilderInjectable vbInjectable = buildInstance(VariableBuilderInjectable.class, injectionNode);

        assertNotNull(vbInjectable.getTarget());
    }

    @Test
    public void testProviderBuilder() throws Exception {

        injectionNodeBuilderRepository.putType(VariableTarget.class, variableInjectionBuilderFactory.buildProviderInjectionNodeBuilder(
                astClassFactory.buildASTClassType(VariableTargetProvider.class)));

        InjectionNode injectionNode = buildInjectionNode(VariableTarget.class);
        ASTType providerType = astClassFactory.buildASTClassType(VariableTargetProvider.class);
        InjectionNode providerInjectionNode = analyzer.analyze(providerType, providerType, context);
        providerInjectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildProviderVariableBuilder(providerInjectionNode));

        VariableTarget target = buildInstance(VariableTarget.class, injectionNode);

        assertNotNull(target);
    }

    private InjectionNode buildInjectionNode(Class<?> instanceClass) {
        InjectionNode injectionNode = new InjectionNode(astClassFactory.buildASTClassType(instanceClass));
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

        ConstructorInjectionPoint noArgConstructorInjectionPoint = new ConstructorInjectionPoint(ASTAccessModifier.PUBLIC, injectionNode.getASTType());
        getInjectionAspect(injectionNode).add(noArgConstructorInjectionPoint);

        return injectionNode;
    }

    private <T> T buildInstance(Class<T> instanceClass, InjectionNode injectionNode) throws Exception {
        PackageClass providerPackageClass = new PackageClass(instanceClass).append("Provider");

        fragmentGeneratorHarness.buildProvider(injectionNode, providerPackageClass);

        ClassLoader classLoader = codeGenerationUtil.build();
        Class<Provider> generatedFactoryClass = (Class<Provider>) classLoader.loadClass(providerPackageClass.getFullyQualifiedName());

        assertNotNull(generatedFactoryClass);
        Provider provider = generatedFactoryClass.newInstance();
        Object result = provider.get();
        assertEquals(instanceClass, result.getClass());

        return (T) result;
    }

    private ASTInjectionAspect getInjectionAspect(InjectionNode injectionNode) {
        if (!injectionNode.containsAspect(ASTInjectionAspect.class)) {
            injectionNode.addAspect(new ASTInjectionAspect());
        }
        return injectionNode.getAspect(ASTInjectionAspect.class);
    }
}
