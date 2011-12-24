package org.androidrobotics;

import android.os.Vibrator;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.ActivityAnalysis;
import org.androidrobotics.analysis.AnalysisRepository;
import org.androidrobotics.analysis.AnalysisRepositoryFactory;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.annotations.Bind;
import org.androidrobotics.annotations.RoboticsModule;
import org.androidrobotics.gen.ActivityGenerator;
import org.androidrobotics.gen.InjectionNodeBuilderRepository;
import org.androidrobotics.gen.VariableBuilderRepositoryFactory;
import org.androidrobotics.gen.variableBuilder.ProviderVariableBuilderFactory;
import org.androidrobotics.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.provider.VibratorProvider;
import org.androidrobotics.util.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Collection;

/**
 * @author John Ericksen
 */
@Singleton
public class RoboticsProcessor {

    private ActivityGenerator activityGenerator;
    private JCodeModel codeModel;
    private Logger logger;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private AnalysisRepositoryFactory analysisRepositoryFactory;
    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private ActivityAnalysis activityAnalysis;

    @Inject
    public RoboticsProcessor(ActivityGenerator activityGenerator,
                             JCodeModel codeModel,
                             Logger logger,
                             VariableBuilderRepositoryFactory variableBuilderRepositoryProvider,
                             ProviderVariableBuilderFactory providerVariableBuilderFactory,
                             VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                             AnalysisRepositoryFactory analysisRepositoryFactory,
                             ActivityAnalysis activityAnalysis) {
        this.activityGenerator = activityGenerator;
        this.codeModel = codeModel;
        this.logger = logger;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.analysisRepositoryFactory = analysisRepositoryFactory;
        this.activityAnalysis = activityAnalysis;

        injectionNodeBuilders = variableBuilderRepositoryProvider.buildRepository();

        //temporary
        injectionNodeBuilders.put(Vibrator.class.getName(), providerVariableBuilderFactory.buildProviderInjectionNodeBuilder(VibratorProvider.class));
    }

    public void processModuleElements(Collection<? extends ASTType> astTypes) {

        for (ASTType astType : astTypes) {
            if (astType.isAnnotated(RoboticsModule.class)) {

                for (ASTMethod astMethod : astType.getMethods()) {
                    if (astMethod.isAnnotated(Bind.class)) {
                        ASTType superType = astMethod.getReturnType();

                        if (astMethod.getParameters().size() == 1) {
                            ASTType implType = astMethod.getParameters().get(0).getASTType();

                            injectionNodeBuilders.put(superType.getName(),
                                    variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(implType));
                        } else {
                            //todo: throw exception
                        }
                    }
                }
            }
        }
    }

    public void processRootElement(Collection<? extends ASTType> astTypes) {

        AnalysisRepository analysisRepository = analysisRepositoryFactory.buildAnalysisRepository();

        for (ASTType astType : astTypes) {

            ActivityDescriptor activityDescriptor = activityAnalysis.analyzeElement(astType, analysisRepository, injectionNodeBuilders);

            if (activityDescriptor != null) {
                try {
                    activityGenerator.generate(activityDescriptor);
                } catch (IOException e) {
                    logger.error("IOException while generating activity", e);
                } catch (JClassAlreadyExistsException e) {
                    logger.error("IOException while generating activity", e);
                } catch (ClassNotFoundException e) {
                    logger.error("IOException while generating activity", e);
                }
            }
        }
    }

    public void verify() {

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
}
