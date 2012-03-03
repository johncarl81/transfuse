package org.androidtransfuse.processor;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidtransfuse.analysis.ActivityAnalysis;
import org.androidtransfuse.analysis.AnalysisRepository;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.ActivityGenerator;
import org.androidtransfuse.model.ActivityDescriptor;
import org.androidtransfuse.model.manifest.Application;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class ComponentProcessor {

    private Logger logger;
    private AnalysisRepository analysisRepository;
    private ActivityAnalysis activityAnalysis;
    private ActivityGenerator activityGenerator;
    private ProcessorContext context;
    private Application application;

    @Inject
    public ComponentProcessor(@Assisted ProcessorContext context,
                              @Assisted Application application,
                              Logger logger,
                              AnalysisRepository analysisRepository,
                              ActivityAnalysis activityAnalysis,
                              ActivityGenerator activityGenerator) {
        this.logger = logger;
        this.analysisRepository = analysisRepository;
        this.activityAnalysis = activityAnalysis;
        this.activityGenerator = activityGenerator;
        this.context = context;
        this.application = application;
    }

    public void processComponent(Collection<? extends ASTType> astTypes) {

        for (ASTType astType : astTypes) {

            ActivityDescriptor activityDescriptor = activityAnalysis.analyzeElement(astType, analysisRepository);

            if (activityDescriptor != null) {

                application.getActivities().add(activityDescriptor.getManifestActivity());

                try {
                    activityGenerator.generate(activityDescriptor, context.getRResource());
                } catch (IOException e) {
                    logger.error("IOException while generating activity", e);
                } catch (JClassAlreadyExistsException e) {
                    logger.error("JClassAlreadyExistsException while generating activity", e);
                } catch (ClassNotFoundException e) {
                    logger.error("ClassNotFoundException while generating activity", e);
                }
            }
        }
    }
}
