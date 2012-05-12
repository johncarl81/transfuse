package org.androidtransfuse.processor;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidtransfuse.analysis.AnalysisRepository;
import org.androidtransfuse.analysis.ApplicationAnalysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.ComponentDescriptor;
import org.androidtransfuse.gen.ComponentGenerator;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ApplicationProcessor {

    private AnalysisRepository analysisRepository;
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
                                ProcessorFactory processorFactory,
                                AnalysisRepository analysisRepository) {
        this.applicationAnalysis = applicationAnalysis;
        this.generator = generator;
        this.logger = logger;
        this.processorFactory = processorFactory;
        this.context = context;
        this.analysisRepository = analysisRepository;
    }

    public ComponentProcessor createComponentProcessor() {
        ComponentDescriptor applicationDescriptor = applicationAnalysis.emptyApplication(context);

        return innerProcessApplication(applicationDescriptor);
    }

    public ComponentProcessor processApplication(ASTType astType) {
        ComponentDescriptor applicationDescriptor = applicationAnalysis.analyzeApplication(context, astType, analysisRepository);

        return innerProcessApplication(applicationDescriptor);
    }

    private ComponentProcessor innerProcessApplication(ComponentDescriptor applicationDescriptor) {
        if (applicationDescriptor != null) {

            try {
                generator.generate(applicationDescriptor);
            } catch (JClassAlreadyExistsException e) {
                logger.error("JClassAlreadyExistsException while generating activity", e);
            }

            return processorFactory.buildComponentProcessor(context, context.getSourceManifest().getApplications().get(0));
        }

        return null; //todo: throw exception?
    }

    public TransfuseAssembler getTransfuseAssembler() {
        return processorFactory.buildAssembler(context);
    }
}
