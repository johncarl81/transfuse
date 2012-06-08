package org.androidtransfuse.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.analysis.ActivityComponentBuilderRepository;
import org.androidtransfuse.analysis.ActivityComponentBuilderRepositoryProvider;
import org.androidtransfuse.analysis.AnalysisContextFactory;
import org.androidtransfuse.analysis.astAnalyzer.BindingRepository;
import org.androidtransfuse.analysis.astAnalyzer.BindingRepositoryProvider;
import org.androidtransfuse.analysis.astAnalyzer.ScopeAspectFactoryRepository;
import org.androidtransfuse.analysis.astAnalyzer.ScopeAspectFactoryRepositoryProvider;
import org.androidtransfuse.analysis.repository.*;
import org.androidtransfuse.gen.InjectionBuilderContextFactory;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.gen.variableBuilder.resource.MethodBasedResourceExpressionBuilderAdaptorFactory;
import org.androidtransfuse.gen.variableBuilder.resource.MethodBasedResourceExpressionBuilderFactory;
import org.androidtransfuse.gen.variableDecorator.ExpressionDecoratorFactory;
import org.androidtransfuse.gen.variableDecorator.VariableExpressionBuilder;
import org.androidtransfuse.gen.variableDecorator.VariableExpressionBuilderFactory;
import org.androidtransfuse.matcher.ComponentGeneratorMatcherProvider;
import org.androidtransfuse.matcher.Matcher;
import org.androidtransfuse.model.manifest.Application;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RResource;

/**
 * @author John Ericksen
 */
public class TransfuseGenerateGuiceModule extends AbstractModule {

    public static final String ORIGINAL_MANIFEST = "originalManifest";
    public static final String MANIFEST_APPLICATION = "manifestApplication";
    public static final String COMPONENT_GENERATOR_MATCHER = "componentGeneratorMatcher";

    private RResource rResource;
    private Manifest manifest;

    public TransfuseGenerateGuiceModule(RResource rResource, Manifest manifest) {
        this.rResource = rResource;
        this.manifest = manifest;
    }

    @Override
    protected void configure() {

        bind(Manifest.class).annotatedWith(Names.named(ORIGINAL_MANIFEST)).toInstance(manifest);
        bind(Application.class).annotatedWith(Names.named(MANIFEST_APPLICATION)).toInstance(manifest.getApplications().get(0));
        bind(RResource.class).toInstance(rResource);

        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder.build(VariableInjectionBuilderFactory.class));
        install(factoryModuleBuilder.build(MethodBasedResourceExpressionBuilderFactory.class));
        install(factoryModuleBuilder.build(MethodBasedResourceExpressionBuilderAdaptorFactory.class));
        install(factoryModuleBuilder.build(VariableExpressionBuilderFactory.class));
        install(factoryModuleBuilder.build(ComponentBuilderFactory.class));
        install(factoryModuleBuilder.build(AnalysisContextFactory.class));
        install(factoryModuleBuilder.build(InjectionBuilderContextFactory.class));

        bind(JCodeModel.class).asEagerSingleton();

        bind(VariableExpressionBuilder.class).toProvider(ExpressionDecoratorFactory.class);
        bind(BindingRepository.class).toProvider(BindingRepositoryProvider.class);
        bind(ScopeAspectFactoryRepository.class).toProvider(ScopeAspectFactoryRepositoryProvider.class);
        bind(InjectionNodeBuilderRepository.class).toProvider(InjectionNodeBuilderRepositoryFactory.class).asEagerSingleton();
        bind(AnalysisRepository.class).toProvider(AnalysisRepositoryFactory.class).asEagerSingleton();
        bind(AOPRepository.class).toProvider(AOPRepositoryProvider.class).asEagerSingleton();
        bind(ActivityComponentBuilderRepository.class).toProvider(ActivityComponentBuilderRepositoryProvider.class).asEagerSingleton();

        bind(Matcher.class).annotatedWith(Names.named(COMPONENT_GENERATOR_MATCHER)).toProvider(ComponentGeneratorMatcherProvider.class);
    }
}
