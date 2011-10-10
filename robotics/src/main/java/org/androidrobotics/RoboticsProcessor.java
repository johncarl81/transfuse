package org.androidrobotics;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.ActivityAnalysis;
import org.androidrobotics.analysis.TypeElementAnalysisBridge;
import org.androidrobotics.gen.ActivityDescriptor;
import org.androidrobotics.gen.ActivityGenerator;
import org.androidrobotics.gen.InjectionGenerator;
import org.androidrobotics.util.FilerSourceCodeWriter;
import org.androidrobotics.util.ResourceCodeWriter;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import java.io.IOException;

/**
 * @author John Ericksen
 */
public class RoboticsProcessor {

    private static final String CODE_COMMENT = "Created by Robotics";

    private static RoboticsProcessor INSTANCE;

    private ActivityAnalysis activityAnalysis;

    private ActivityGenerator activityGenerator;
    private JCodeModel codeModel;
    private InjectionGenerator injectionGenerator;

    private RoboticsProcessor() {
        //private singleton constructor

        codeModel = new JCodeModel();
        activityAnalysis = new ActivityAnalysis();
        injectionGenerator = new InjectionGenerator(codeModel);
        activityGenerator = new ActivityGenerator(codeModel, injectionGenerator);

    }

    public static RoboticsProcessor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RoboticsProcessor();
        }

        return INSTANCE;
    }

    public void processRootElement(TypeElement element) {
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
