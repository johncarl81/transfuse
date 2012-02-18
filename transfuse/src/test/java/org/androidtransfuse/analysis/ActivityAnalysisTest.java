package org.androidtransfuse.analysis;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.targets.MockActivityDelegate;
import org.androidtransfuse.config.TransfuseGenerationGuiceModule;
import org.androidtransfuse.gen.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.VariableBuilderRepositoryFactory;
import org.androidtransfuse.model.ActivityDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.util.JavaUtilLogger;
import org.junit.Before;
import org.junit.Test;

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
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new TransfuseGenerationGuiceModule(new JavaUtilLogger(this)));

        InjectionNodeBuilderRepository injectionNodeBuilderRepository = injector.getInstance(VariableBuilderRepositoryFactory.class).buildRepository();
        AnalysisRepository analysisRepository = injector.getInstance(AnalysisRepositoryFactory.class).buildAnalysisRepository();

        ActivityAnalysis activityAnalysis = injector.getInstance(ActivityAnalysis.class);

        ASTClassFactory astClassFactory = injector.getInstance(ASTClassFactory.class);
        AOPRepository aopRepository = injector.getProvider(AOPRepository.class).get();

        activityDescriptor = activityAnalysis.analyzeElement(astClassFactory.buildASTClassType(MockActivityDelegate.class), analysisRepository, injectionNodeBuilderRepository, aopRepository);
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
        List<InjectionNode> injectionNodes = activityDescriptor.getInjectionNodes();

        assertEquals(1, injectionNodes.size());
        InjectionNode injectionNode = injectionNodes.get(0);
        assertEquals(MockActivityDelegate.class.getName(), injectionNode.getClassName());
    }


}
