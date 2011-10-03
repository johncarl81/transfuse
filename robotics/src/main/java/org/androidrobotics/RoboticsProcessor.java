package org.androidrobotics;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.ActivityAnalysis;
import org.androidrobotics.gen.ActivityDescriptor;
import org.androidrobotics.gen.ActivityGenerator;
import org.androidrobotics.util.AnnotatedElementBridge;
import org.androidrobotics.util.FilerSourceCodeWriter;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import java.io.IOException;

/**
 * @author John Ericksen
 */
public class RoboticsProcessor {

    private static RoboticsProcessor INSTANCE;

    private ActivityAnalysis activityAnalysis;

    private ActivityGenerator activityGenerator;
    private JCodeModel codeModel;

    private RoboticsProcessor() {
        //private singleton constructor

        codeModel = new JCodeModel();
        activityAnalysis = new ActivityAnalysis();
        activityGenerator = new ActivityGenerator(codeModel);
    }

    public static RoboticsProcessor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RoboticsProcessor();
        }

        return INSTANCE;
    }

    public void processRootElement(Element element) {
        ActivityDescriptor activityDescriptor = activityAnalysis.analyzeElement(new AnnotatedElementBridge(element));

        if (activityDescriptor != null) {
            try {
                activityGenerator.generate(activityDescriptor);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JClassAlreadyExistsException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeSource(Filer filer) {

        try {
            codeModel.build(new FilerSourceCodeWriter(filer));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }
}
