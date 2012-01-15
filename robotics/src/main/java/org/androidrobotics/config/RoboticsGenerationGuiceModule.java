package org.androidrobotics.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.astAnalyzer.BindingRepository;
import org.androidrobotics.analysis.astAnalyzer.BindingRepositoryProvider;
import org.androidrobotics.gen.InjectionBuilderContextFactory;
import org.androidrobotics.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidrobotics.gen.variableBuilder.resource.MethodBasedResourceExpressionBuilderAdaptorFactory;
import org.androidrobotics.gen.variableBuilder.resource.MethodBasedResourceExpressionBuilderFactory;
import org.androidrobotics.util.Logger;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * @author John Ericksen
 */
public class RoboticsGenerationGuiceModule extends AbstractModule {

    private Logger logger;
    private ProcessingEnvironment processingEnv;

    public RoboticsGenerationGuiceModule(Logger logger, ProcessingEnvironment processingEnv) {
        this.logger = logger;
        this.processingEnv = processingEnv;
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

        bind(JCodeModel.class).asEagerSingleton();

        bind(Logger.class).toInstance(logger);
        bind(BindingRepository.class).toProvider(BindingRepositoryProvider.class);
        bind(ProcessingEnvironment.class).toInstance(processingEnv);
    }
}
