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
    private ComponentGenerator generator;
    private Provider<ComponentProcessor> componentProcessorProvider;
    private Provider<TransfuseAssembler> transfuseAssemblerProvider;

    @Inject
    public ApplicationGenerator(ApplicationAnalysis applicationAnalysis,
                                ComponentGenerator generator,
                                Provider<TransfuseAssembler> transfuseAssemblerProvider,
                                Provider<ComponentProcessor> componentProcessorProvider) {
        this.applicationAnalysis = applicationAnalysis;
        this.generator = generator;
        this.transfuseAssemblerProvider = transfuseAssemblerProvider;
        this.componentProcessorProvider = componentProcessorProvider;
    }

    public void generate() {
        applicationAnalysis.emptyApplication();
    }

    public void generate(ASTType astType) {
        ComponentDescriptor applicationDescriptor = applicationAnalysis.analyze(astType);

        if (applicationDescriptor != null) {
            generator.generate(applicationDescriptor);
        }
    }

    public ComponentProcessor getComponentProcessor() {
        return componentProcessorProvider.get();
    }

    public TransfuseAssembler getTransfuseAssembler() {
        return transfuseAssemblerProvider.get();
    }
}
