package org.androidtransfuse.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.sun.codemodel.JCodeModel;
import com.thoughtworks.xstream.XStream;
import org.androidtransfuse.analysis.adapter.ASTElementAnnotationFactory;
import org.androidtransfuse.analysis.astAnalyzer.BindingRepository;
import org.androidtransfuse.analysis.astAnalyzer.BindingRepositoryProvider;
import org.androidtransfuse.gen.InjectionBuilderContextFactory;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.gen.variableBuilder.resource.MethodBasedResourceExpressionBuilderAdaptorFactory;
import org.androidtransfuse.gen.variableBuilder.resource.MethodBasedResourceExpressionBuilderFactory;
import org.androidtransfuse.processor.ProcessorFactory;
import org.androidtransfuse.util.Logger;
import org.androidtransfuse.util.ManifestLocatorFactory;

/**
 * @author John Ericksen
 */
public class TransfuseGenerationGuiceModule extends AbstractModule {

    private Logger logger;

    public TransfuseGenerationGuiceModule(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void configure() {
        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder.build(InjectionBuilderContextFactory.class));
        install(factoryModuleBuilder.build(VariableInjectionBuilderFactory.class));
        install(factoryModuleBuilder.build(MethodBasedResourceExpressionBuilderFactory.class));
        install(factoryModuleBuilder.build(MethodBasedResourceExpressionBuilderAdaptorFactory.class));
        install(factoryModuleBuilder.build(ASTElementAnnotationFactory.class));
        install(factoryModuleBuilder.build(ManifestLocatorFactory.class));
        install(factoryModuleBuilder.build(ProcessorFactory.class));

        bind(JCodeModel.class).asEagerSingleton();

        bind(Logger.class).toInstance(logger);
        bind(BindingRepository.class).toProvider(BindingRepositoryProvider.class);
        bind(XStream.class).toProvider(XStreamProvider.class);
    }
}
