package org.androidtransfuse.processor;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidtransfuse.analysis.AnalysisRepository;
import org.androidtransfuse.analysis.ApplicationAnalysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.ApplicationGenerator;
import org.androidtransfuse.model.ApplicationDescriptor;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author John Ericksen
 */
public class ApplicationProcessor {

    private AnalysisRepository analysisRepository;
    private ApplicationAnalysis applicationAnalysis;
    private ApplicationGenerator applicationGenerator;
    private Logger logger;
    private ProcessorFactory processorFactory;
    private ProcessorContext context;

    @Inject
    public ApplicationProcessor(@Assisted ProcessorContext context,
                                ApplicationAnalysis applicationAnalysis,
                                ApplicationGenerator applicationGenerator,
                                Logger logger,
                                ProcessorFactory processorFactory,
                                AnalysisRepository analysisRepository) {
        this.applicationAnalysis = applicationAnalysis;
        this.applicationGenerator = applicationGenerator;
        this.logger = logger;
        this.processorFactory = processorFactory;
        this.context = context;
        this.analysisRepository = analysisRepository;
    }

    public ComponentProcessor createComponentProcessor() {
        ApplicationDescriptor applicationDescriptor = applicationAnalysis.emptyApplication(context.getManifest().getApplicationPackage());

        return innerProcessApplication(applicationDescriptor);
    }

    public ComponentProcessor processApplication(ASTType astType) {
        ApplicationDescriptor applicationDescriptor = applicationAnalysis.analyzeApplication(astType, analysisRepository);

        return innerProcessApplication(applicationDescriptor);
    }

    private ComponentProcessor innerProcessApplication(ApplicationDescriptor applicationDescriptor) {
        if (applicationDescriptor != null) {

            context.getSourceManifest().getApplications().add(applicationDescriptor.getManifestApplication());

            try {
                applicationGenerator.generate(applicationDescriptor, context.getRResource());
            } catch (IOException e) {
                logger.error("IOException while generating activity", e);
            } catch (JClassAlreadyExistsException e) {
                logger.error("JClassAlreadyExistsException while generating activity", e);
            } catch (ClassNotFoundException e) {
                logger.error("ClassNotFoundException while generating activity", e);
            }

            return processorFactory.buildComponentProcessor(context, applicationDescriptor.getManifestApplication());
        }

        return null; //todo: throw exception?
    }

    public TransfuseAssembler getTransfuseAssembler() {
        return processorFactory.buildAssembler(context);
    }
}
