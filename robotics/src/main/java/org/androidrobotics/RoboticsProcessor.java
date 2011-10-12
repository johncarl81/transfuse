package org.androidrobotics;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.ActivityAnalysis;
import org.androidrobotics.analysis.TypeElementAnalysisBridge;
import org.androidrobotics.annotations.Activity;
import org.androidrobotics.gen.ActivityDescriptor;
import org.androidrobotics.gen.ActivityGenerator;
import org.androidrobotics.util.FilerSourceCodeWriter;
import org.androidrobotics.util.ResourceCodeWriter;

import javax.annotation.processing.Filer;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.TypeElement;
import java.io.IOException;

/**
 * @author John Ericksen
 */
@Singleton
public class RoboticsProcessor {

    @Inject
    private ActivityAnalysis activityAnalysis;
    @Inject
    private ActivityGenerator activityGenerator;
    @Inject
    private JCodeModel codeModel;

    public void processRootElement(TypeElement element) {
        if (element.getAnnotation(Activity.class) != null) {
            ActivityDescriptor activityDescriptor = activityAnalysis.analyzeElement(new TypeElementAnalysisBridge(element));

            if (activityDescriptor != null) {
                try {
                    activityGenerator.generate(activityDescriptor);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JClassAlreadyExistsException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void writeSource(Filer filer) {

        try {
            codeModel.build(
                    new FilerSourceCodeWriter(filer),
                    new ResourceCodeWriter(filer));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }
}
