package org.androidtransfuse.processor;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidtransfuse.analysis.ApplicationAnalysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.ComponentGenerator;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ApplicationProcessor {

    private ApplicationAnalysis applicationAnalysis;
    private ComponentGenerator generator;
    private Logger logger;
    private ProcessorFactory processorFactory;
    private ProcessorContext context;

    @Inject
    public ApplicationProcessor(@Assisted ProcessorContext context,
                                ApplicationAnalysis applicationAnalysis,
                                ComponentGenerator generator,
                                Logger logger,
                                ProcessorFactory processorFactory) {
        this.applicationAnalysis = applicationAnalysis;
        this.generator = generator;
        this.logger = logger;
        this.processorFactory = processorFactory;
        this.context = context;
    }

    public ComponentProcessor createComponentProcessor() {
        applicationAnalysis.emptyApplication(context);

        return buildComponentProcessor();
    }

    public ComponentProcessor processApplication(ASTType astType) {
        ComponentDescriptor applicationDescriptor = applicationAnalysis.analyzeApplication(context, astType);

        if (applicationDescriptor != null) {

            try {
                generator.generate(applicationDescriptor);
            } catch (JClassAlreadyExistsException e) {
                logger.error("JClassAlreadyExistsException while generating activity", e);
            }
        }

        return buildComponentProcessor();
    }

    private ComponentProcessor buildComponentProcessor() {
        return processorFactory.buildComponentProcessor(context, context.getSourceManifest().getApplications().get(0));
    }

    public TransfuseAssembler getTransfuseAssembler() {
        return processorFactory.buildAssembler(context);
    }
}
