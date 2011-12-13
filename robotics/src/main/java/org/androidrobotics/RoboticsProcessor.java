package org.androidrobotics;

import android.app.Activity;
import android.os.Vibrator;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.ActivityAnalysis;
import org.androidrobotics.analysis.AnalysisRepositoryFactory;
import org.androidrobotics.analysis.AnalysisRepositoryProvider;
import org.androidrobotics.analysis.InjectionPointFactory;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.annotations.Bind;
import org.androidrobotics.annotations.RoboticsModule;
import org.androidrobotics.gen.*;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.provider.VibratorProvider;
import org.androidrobotics.util.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
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
    private AnalysisRepositoryProvider analysisRepositoryProvider;

    private VariableBuilderRepository variableBuilders;

    @Inject
    public RoboticsProcessor(ActivityGenerator activityGenerator,
                             JCodeModel codeModel,
                             Logger logger,
                             VariableBuilderRepositoryFactory variableBuilderRepositoryProvider,
                             Provider<PotentialVariableBuilder> potentialVariableBuilderProvider,
                             ProviderVariableBuilderFactory providerVariableBuilderFactory,
                             VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                             AnalysisRepositoryProvider analysisRepositoryProvider) {
        this.activityGenerator = activityGenerator;
        this.codeModel = codeModel;
        this.logger = logger;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;

        variableBuilders = variableBuilderRepositoryProvider.buildRepository();

        //temporary
        variableBuilders.put(android.content.Context.class.getName(), potentialVariableBuilderProvider.get());
        variableBuilders.put(Activity.class.getName(), potentialVariableBuilderProvider.get());
        variableBuilders.put(Vibrator.class.getName(), providerVariableBuilderFactory.buildProviderVariableBuilder(VibratorProvider.class));

        this.analysisRepositoryProvider = analysisRepositoryProvider;

    }

    public void processModuleElements(Collection<? extends ASTType> astTypes) {


        for (ASTType astType : astTypes) {
            if (astType.isAnnotated(RoboticsModule.class)) {

                for (ASTMethod astMethod : astType.getMethods()) {
                    if (astMethod.isAnnotated(Bind.class)) {
                        ASTType superType = astMethod.getReturnType();

                        if (astMethod.getParameters().size() == 1) {
                            ASTType implType = astMethod.getParameters().get(0).getASTType();

                            System.out.println("Associating: " + superType.getName() + " with: " + implType.getName());

                            variableBuilders.put(superType.getName(),
                                    variableInjectionBuilderFactory.buildVariableInjectionBuilder(implType));
                        } else {
                            //todo: throw exception
                        }
                    }
                }
            }
        }

        analysisRepositoryProvider.setAnalysisRepository(new AnalysisRepositoryFactory(new InjectionPointFactory(variableBuilders)).buildAnalysisRepository());
    }

    public void processRootElement(Collection<? extends ASTType> astTypes) {
        for (ASTType astType : astTypes) {
            ActivityDescriptor activityDescriptor = new ActivityAnalysis(new InjectionPointFactory(variableBuilders)).analyzeElement(astType);

            if (activityDescriptor != null) {
                try {
                    activityGenerator.generate(activityDescriptor, variableBuilders);
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
