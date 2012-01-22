package org.androidrobotics;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.ActivityAnalysis;
import org.androidrobotics.analysis.AnalysisRepository;
import org.androidrobotics.analysis.AnalysisRepositoryFactory;
import org.androidrobotics.analysis.ModuleProcessor;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.annotations.RoboticsModule;
import org.androidrobotics.gen.ActivityGenerator;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.model.manifest.Activity;
import org.androidrobotics.model.manifest.Application;
import org.androidrobotics.model.manifest.Manifest;
import org.androidrobotics.model.r.RResource;
import org.androidrobotics.util.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
@Singleton
public class RoboticsProcessor {

    private ActivityGenerator activityGenerator;
    private JCodeModel codeModel;
    private Logger logger;
    private AnalysisRepositoryFactory analysisRepositoryFactory;
    private ActivityAnalysis activityAnalysis;
    private ModuleProcessor moduleProcessor;

    private Manifest manifest = null;
    private RResource rResource = null;

    @Inject
    public RoboticsProcessor(ActivityGenerator activityGenerator,
                             JCodeModel codeModel,
                             Logger logger,
                             AnalysisRepositoryFactory analysisRepositoryFactory,
                             ActivityAnalysis activityAnalysis,
                             ModuleProcessor moduleProcessor) {
        this.activityGenerator = activityGenerator;
        this.codeModel = codeModel;
        this.logger = logger;
        this.analysisRepositoryFactory = analysisRepositoryFactory;
        this.activityAnalysis = activityAnalysis;
        this.moduleProcessor = moduleProcessor;
    }

    public void processModule(Collection<? extends ASTType> astTypes) {

        for (ASTType astType : astTypes) {
            if (astType.isAnnotated(RoboticsModule.class)) {

                for (ASTMethod astMethod : astType.getMethods()) {
                    moduleProcessor.processMethod(astMethod);
                }
            }
        }
    }

    public void processComponent(Collection<? extends ASTType> astTypes) {

        AnalysisRepository analysisRepository = analysisRepositoryFactory.buildAnalysisRepository();

        Application application = manifest.getApplications().get(0);

        Set<Activity> activities = application.getActivities();

        Set<Activity> unreferencedActivities = new HashSet<Activity>(activities);

        for (ASTType astType : astTypes) {

            ActivityDescriptor activityDescriptor = activityAnalysis.analyzeElement(astType, analysisRepository, moduleProcessor.getInjectionNodeBuilders(), moduleProcessor.getAOPRepository());


            if (activityDescriptor != null) {
                try {
                    activityGenerator.generate(activityDescriptor, rResource);

                    Activity activity = new Activity("." + activityDescriptor.getPackageClass().getClassName(), activityDescriptor.getLabel());

                    activities.add(activity);
                    unreferencedActivities.remove(activity);
                } catch (IOException e) {
                    logger.error("IOException while generating activity", e);
                } catch (JClassAlreadyExistsException e) {
                    logger.error("JClassAlreadyExistsException while generating activity", e);
                } catch (ClassNotFoundException e) {
                    logger.error("ClassNotFoundException while generating activity", e);
                }
            }
        }

        activities.removeAll(unreferencedActivities);
    }

    public void writeSource(CodeWriter codeWriter, CodeWriter resourceWriter) {

        try {
            codeModel.build(
                    codeWriter,
                    resourceWriter);

        } catch (IOException e) {
            logger.error("Error while writing source files", e);
        }
    }

    public void processManifest(Manifest manifest) {
        this.manifest = manifest;
    }

    public void processR(RResource rResource) {
        this.rResource = rResource;
    }

    public Manifest getManifest() {
        return manifest;
    }
}
