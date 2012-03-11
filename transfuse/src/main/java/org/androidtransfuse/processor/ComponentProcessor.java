package org.androidtransfuse.processor;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidtransfuse.analysis.ActivityAnalysis;
import org.androidtransfuse.analysis.AnalysisRepository;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.AndroidComponentDescriptor;
import org.androidtransfuse.gen.AndroidGenerator;
import org.androidtransfuse.model.manifest.Application;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class ComponentProcessor {

    private Logger logger;
    private AnalysisRepository analysisRepository;
    private ActivityAnalysis activityAnalysis;
    private AndroidGenerator generator;
    private ProcessorContext context;
    private Application application;

    @Inject
    public ComponentProcessor(@Assisted ProcessorContext context,
                              @Assisted Application application,
                              Logger logger,
                              AnalysisRepository analysisRepository,
                              ActivityAnalysis activityAnalysis,
                              AndroidGenerator generator) {
        this.logger = logger;
        this.analysisRepository = analysisRepository;
        this.activityAnalysis = activityAnalysis;
        this.generator = generator;
        this.context = context;
        this.application = application;
    }

    public void processComponent(Collection<? extends ASTType> astTypes) {

        for (ASTType astType : astTypes) {

            AndroidComponentDescriptor activityDescriptor = activityAnalysis.analyzeElement(astType, analysisRepository, application);

            if (activityDescriptor != null) {

                try {
                    generator.generate(activityDescriptor, context.getRResource());
                } catch (JClassAlreadyExistsException e) {
                    logger.error("JClassAlreadyExistsException while generating activity", e);
                }
            }
        }
    }
}
