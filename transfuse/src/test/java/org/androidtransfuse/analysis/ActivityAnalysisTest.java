package org.androidtransfuse.analysis;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.targets.MockActivityDelegate;
import org.androidtransfuse.config.TransfuseGenerationGuiceModule;
import org.androidtransfuse.gen.ComponentBuilder;
import org.androidtransfuse.gen.ComponentDescriptor;
import org.androidtransfuse.model.manifest.Application;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.processor.ProcessorContext;
import org.androidtransfuse.processor.ProcessorFactory;
import org.androidtransfuse.util.EmptyRResource;
import org.androidtransfuse.util.JavaUtilLogger;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class ActivityAnalysisTest {

    public static final String TEST_NAME = "ActivityTestTarget";
    public static final int TEST_LAYOUT_ID = 123456;

    private ComponentDescriptor activityDescriptor;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new TransfuseGenerationGuiceModule(new JavaUtilLogger(this)));

        AnalysisRepository analysisRepository = injector.getInstance(AnalysisRepositoryFactory.class).get();

        ActivityAnalysis activityAnalysis = injector.getInstance(ActivityAnalysis.class);

        ASTClassFactory astClassFactory = injector.getInstance(ASTClassFactory.class);
        ProcessorFactory processorFactory = injector.getInstance(ProcessorFactory.class);

        Application manifestApplication = new Application();
        ModuleProcessor moduleProcessor = injector.getInstance(ModuleProcessor.class);
        ProcessorContext processorContext = processorFactory.buildContext(new EmptyRResource(), new Manifest(), moduleProcessor);

        activityDescriptor = activityAnalysis.analyzeElement(astClassFactory.buildASTClassType(MockActivityDelegate.class), analysisRepository, manifestApplication, processorContext);
    }

    @Test
    public void testActivityAnnotation() {
        assertEquals(TEST_NAME, activityDescriptor.getPackageClass().getClassName());
    }

    @Test
    public void testDelegateInjectionPoint() {
        Set<ComponentBuilder> componentBuilders = activityDescriptor.getComponentBuilders();

        assertEquals(1, componentBuilders.size());
        //ComponentBuilder componentBuilder = componentBuilders.iterator().next();
        //assertEquals(MockActivityDelegate.class.getName(), ((OnCreateComponentBuilder)componentBuilder).
    }


}
