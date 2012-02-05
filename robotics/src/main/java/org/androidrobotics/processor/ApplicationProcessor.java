package org.androidrobotics.processor;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidrobotics.analysis.AnalysisRepository;
import org.androidrobotics.analysis.AnalysisRepositoryFactory;
import org.androidrobotics.analysis.ApplicationAnalysis;
import org.androidrobotics.analysis.ModuleProcessor;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.ApplicationGenerator;
import org.androidrobotics.model.ApplicationDescriptor;
import org.androidrobotics.util.Logger;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author John Ericksen
 */
public class ApplicationProcessor {

    private AnalysisRepositoryFactory analysisRepositoryFactory;
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
                                AnalysisRepositoryFactory analysisRepositoryFactory) {
        this.applicationAnalysis = applicationAnalysis;
        this.applicationGenerator = applicationGenerator;
        this.logger = logger;
        this.processorFactory = processorFactory;
        this.context = context;
        this.analysisRepositoryFactory = analysisRepositoryFactory;
    }

    public ComponentProcessor processApplication(ASTType astType) {
        AnalysisRepository analysisRepository = analysisRepositoryFactory.buildAnalysisRepository();

        ModuleProcessor moduleProcessor = context.getModuleProcessor();

        ApplicationDescriptor applicationDescriptor = applicationAnalysis.analyzeApplication(astType, analysisRepository, moduleProcessor.getInjectionNodeBuilders(), moduleProcessor.getAOPRepository());

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

    public RoboticsAssembler getRoboticsAssembler() {
        return processorFactory.buildAssembler(context);
    }
}
