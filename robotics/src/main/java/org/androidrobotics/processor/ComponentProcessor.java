package org.androidrobotics.processor;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidrobotics.analysis.ActivityAnalysis;
import org.androidrobotics.analysis.AnalysisRepository;
import org.androidrobotics.analysis.AnalysisRepositoryFactory;
import org.androidrobotics.analysis.ModuleProcessor;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.ActivityGenerator;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.model.manifest.Activity;
import org.androidrobotics.util.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class ComponentProcessor {

    private Logger logger;
    private AnalysisRepositoryFactory analysisRepositoryFactory;
    private ActivityAnalysis activityAnalysis;
    private ActivityGenerator activityGenerator;
    private ProcessorFactory processorFactory;
    private ProcessorContext context;

    @Inject
    public ComponentProcessor(@Assisted ProcessorContext context,
                              Logger logger,
                              AnalysisRepositoryFactory analysisRepositoryFactory,
                              ActivityAnalysis activityAnalysis,
                              ActivityGenerator activityGenerator,
                              ProcessorFactory processorFactory) {
        this.logger = logger;
        this.analysisRepositoryFactory = analysisRepositoryFactory;
        this.activityAnalysis = activityAnalysis;
        this.activityGenerator = activityGenerator;
        this.processorFactory = processorFactory;
        this.context = context;
    }

    public void processComponent(Collection<? extends ASTType> astTypes) {

        AnalysisRepository analysisRepository = analysisRepositoryFactory.buildAnalysisRepository();

        ModuleProcessor moduleProcessor = context.getModuleProcessor();

        for (ASTType astType : astTypes) {

            ActivityDescriptor activityDescriptor = activityAnalysis.analyzeElement(astType, analysisRepository, moduleProcessor.getInjectionNodeBuilders(), moduleProcessor.getAOPRepository());

            if (activityDescriptor != null) {
                try {
                    activityGenerator.generate(activityDescriptor, context.getRResource());

                    Activity activity = new Activity("." + activityDescriptor.getPackageClass().getClassName(), activityDescriptor.getLabel());

                    activity.setTag("yes");

                    //activities.add(activity);
                    //unreferencedActivities.remove(activity);
                } catch (IOException e) {
                    logger.error("IOException while generating activity", e);
                } catch (JClassAlreadyExistsException e) {
                    logger.error("JClassAlreadyExistsException while generating activity", e);
                } catch (ClassNotFoundException e) {
                    logger.error("ClassNotFoundException while generating activity", e);
                }
            }
        }

        //activities.removeAll(unreferencedActivities);
    }

    public RoboticsAssembler getRoboticsAssembler() {
        return processorFactory.buildAssembler(context);
    }
}
