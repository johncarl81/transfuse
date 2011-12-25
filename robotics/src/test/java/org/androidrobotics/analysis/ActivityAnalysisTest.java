package org.androidrobotics.analysis;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.targets.MockActivityDelegate;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.gen.InjectionNodeBuilderRepository;
import org.androidrobotics.gen.VariableBuilderRepositoryFactory;
import org.androidrobotics.gen.variableBuilder.ContextVariableInjectionNodeBuilder;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.util.JavaUtilLogger;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Provider;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class ActivityAnalysisTest {

    public static final String TEST_NAME = "ActivityTestTarget";
    public static final int TEST_LAYOUT_ID = 123456;

    private ActivityDescriptor activityDescriptor;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this)));

        InjectionNodeBuilderRepository injectionNodeBuilderRepository = injector.getInstance(VariableBuilderRepositoryFactory.class).buildRepository();
        AnalysisRepository analysisRepository = injector.getInstance(AnalysisRepositoryFactory.class).buildAnalysisRepository();
        Provider<ContextVariableInjectionNodeBuilder> contextVariableInjectionNodeBuilderProviderBuilderProvider = injector.getProvider(ContextVariableInjectionNodeBuilder.class);
        VariableBuilderRepositoryFactory variableBuilderRepositoryFactory = injector.getInstance(VariableBuilderRepositoryFactory.class);
        InjectionPointFactory injectionPointFactory = injector.getInstance(InjectionPointFactory.class);
        ActivityAnalysis activityAnalysis = new ActivityAnalysis(injectionPointFactory, contextVariableInjectionNodeBuilderProviderBuilderProvider, variableBuilderRepositoryFactory);
        ASTClassFactory astClassFactory = injector.getInstance(ASTClassFactory.class);
        InterceptorRepository interceptorRepository = injector.getInstance(InterceptorRepository.class);

        activityDescriptor = activityAnalysis.analyzeElement(astClassFactory.buildASTClassType(MockActivityDelegate.class), analysisRepository, injectionNodeBuilderRepository, interceptorRepository);
    }

    @Test
    public void testActivityAnnotation() {
        assertEquals(TEST_NAME, activityDescriptor.getPackageClass().getClassName());
    }

    @Test
    public void testLayoutAnnotation() {
        assertEquals(TEST_LAYOUT_ID, activityDescriptor.getLayout());
    }

    @Test
    public void testDelegateInjectionPoint() {
        List<FieldInjectionPoint> injectionPoints = activityDescriptor.getInjectionPoints();

        assertEquals(1, injectionPoints.size());
        FieldInjectionPoint injectionPoint = injectionPoints.get(0);
        assertEquals(MockActivityDelegate.class.getName(), injectionPoint.getName());
    }


}
