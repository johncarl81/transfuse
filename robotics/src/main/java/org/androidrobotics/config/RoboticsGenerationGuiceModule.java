package org.androidrobotics.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.sun.codemodel.JCodeModel;
import com.thoughtworks.xstream.XStream;
import org.androidrobotics.analysis.adapter.ASTElementAnnotationFactory;
import org.androidrobotics.analysis.astAnalyzer.BindingRepository;
import org.androidrobotics.analysis.astAnalyzer.BindingRepositoryProvider;
import org.androidrobotics.gen.InjectionBuilderContextFactory;
import org.androidrobotics.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidrobotics.gen.variableBuilder.resource.MethodBasedResourceExpressionBuilderAdaptorFactory;
import org.androidrobotics.gen.variableBuilder.resource.MethodBasedResourceExpressionBuilderFactory;
import org.androidrobotics.util.Logger;
import org.androidrobotics.util.ManifestLocatorFactory;

/**
 * @author John Ericksen
 */
public class RoboticsGenerationGuiceModule extends AbstractModule {

    private Logger logger;

    public RoboticsGenerationGuiceModule(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void configure() {
        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .build(InjectionBuilderContextFactory.class));

        install(factoryModuleBuilder
                .build(VariableInjectionBuilderFactory.class));

        install(factoryModuleBuilder
                .build(MethodBasedResourceExpressionBuilderFactory.class));

        install(factoryModuleBuilder
                .build(MethodBasedResourceExpressionBuilderAdaptorFactory.class));

        install(factoryModuleBuilder
                .build(ASTElementAnnotationFactory.class));

        install(factoryModuleBuilder
                .build(ManifestLocatorFactory.class));

        bind(JCodeModel.class).asEagerSingleton();

        bind(Logger.class).toInstance(logger);
        bind(BindingRepository.class).toProvider(BindingRepositoryProvider.class);
        bind(XStream.class).toProvider(XStreamProvider.class);
    }
}
