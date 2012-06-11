package org.androidtransfuse.gen;

import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidtransfuse.analysis.ApplicationAnalysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.processor.ComponentProcessor;
import org.androidtransfuse.processor.TransfuseAssembler;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ApplicationGenerator implements Generator {

    private ApplicationAnalysis applicationAnalysis;
    private ComponentGenerator generator;
    private Logger logger;
    private Provider<ComponentProcessor> componentProcessorProvider;
    private Provider<TransfuseAssembler> transfuseAssemblerProvider;

    @Inject
    public ApplicationGenerator(ApplicationAnalysis applicationAnalysis,
                                ComponentGenerator generator,
                                Logger logger,
                                Provider<TransfuseAssembler> transfuseAssemblerProvider,
                                Provider<ComponentProcessor> componentProcessorProvider) {
        this.applicationAnalysis = applicationAnalysis;
        this.generator = generator;
        this.logger = logger;
        this.transfuseAssemblerProvider = transfuseAssemblerProvider;
        this.componentProcessorProvider = componentProcessorProvider;
    }

    public void generate() {
        applicationAnalysis.emptyApplication();
    }

    public void generate(ASTType astType) {
        ComponentDescriptor applicationDescriptor = applicationAnalysis.analyzeApplication(astType);

        if (applicationDescriptor != null) {

            try {
                generator.generate(applicationDescriptor);
            } catch (JClassAlreadyExistsException e) {
                logger.error("JClassAlreadyExistsException while generating activity", e);
            }
        }
    }

    public ComponentProcessor getComponentProcessor() {
        return componentProcessorProvider.get();
    }

    public TransfuseAssembler getTransfuseAssembler() {
        return transfuseAssemblerProvider.get();
    }
}
