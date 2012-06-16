package org.androidtransfuse.gen;

import org.androidtransfuse.analysis.ApplicationAnalysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.processor.ComponentProcessor;
import org.androidtransfuse.processor.TransfuseAssembler;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ApplicationGenerator implements Generator<ASTType> {

    private ApplicationAnalysis applicationAnalysis;
    private Provider<ComponentProcessor> componentProcessorProvider;
    private Provider<TransfuseAssembler> transfuseAssemblerProvider;
    private AnalysisGeneration<ComponentDescriptor> analysisGeneration;

    @Inject
    public ApplicationGenerator(ApplicationAnalysis applicationAnalysis,
                                ComponentGenerator generator,
                                AnalysisGenerationFactory analysisGenerationFactory,
                                Provider<TransfuseAssembler> transfuseAssemblerProvider,
                                Provider<ComponentProcessor> componentProcessorProvider) {
        this.analysisGeneration = analysisGenerationFactory.buildAnalysisGeneration(applicationAnalysis, generator);
        this.applicationAnalysis = applicationAnalysis;
        this.transfuseAssemblerProvider = transfuseAssemblerProvider;
        this.componentProcessorProvider = componentProcessorProvider;
    }

    public void generate() {
        applicationAnalysis.emptyApplication();
    }

    public void generate(ASTType astType) {
        analysisGeneration.generate(astType);
    }

    public ComponentProcessor getComponentProcessor() {
        return componentProcessorProvider.get();
    }

    public TransfuseAssembler getTransfuseAssembler() {
        return transfuseAssemblerProvider.get();
    }
}
