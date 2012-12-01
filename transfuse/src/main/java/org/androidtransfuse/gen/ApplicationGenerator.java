package org.androidtransfuse.gen;

import org.androidtransfuse.processor.AnalysisGenerationTransactionProcessorBuilderFactory;
import org.androidtransfuse.processor.ComponentProcessor;
import org.androidtransfuse.processor.TransfuseAssembler;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ApplicationGenerator {

    private final Provider<ComponentProcessor> componentProcessorProvider;
    private final Provider<TransfuseAssembler> transfuseAssemblerProvider;
    private final ManifestBuilder manifestBuilder;

    @Inject
    public ApplicationGenerator(Provider<TransfuseAssembler> transfuseAssemblerProvider,
                                Provider<ComponentProcessor> componentProcessorProvider,
                                AnalysisGenerationTransactionProcessorBuilderFactory processorFactory,
                                ManifestBuilder manifestBuilder) {
        this.transfuseAssemblerProvider = transfuseAssemblerProvider;
        this.componentProcessorProvider = componentProcessorProvider;
        this.manifestBuilder = manifestBuilder;
    }

    public void generateEmptyApplication() {
        manifestBuilder.setupManifestApplication(android.app.Application.class.getName());
    }

    public ComponentProcessor getComponentProcessor() {
        return componentProcessorProvider.get();
    }

    public TransfuseAssembler getTransfuseAssembler() {
        return transfuseAssemblerProvider.get();
    }
}
