package org.androidrobotics;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import org.androidrobotics.analysis.ActivityAnalysis;
import org.androidrobotics.analysis.ElementAnalysisBridge;
import org.androidrobotics.gen.ActivityDescriptor;
import org.androidrobotics.gen.ActivityGenerator;
import org.androidrobotics.gen.InjectionGenerator;
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
    private InjectionGenerator injectonGenerator;

    private RoboticsProcessor() {
        //private singleton constructor

        codeModel = new JCodeModel();
        activityAnalysis = new ActivityAnalysis();
        activityGenerator = new ActivityGenerator(codeModel);
        injectonGenerator = new InjectionGenerator(codeModel);
    }

    public static RoboticsProcessor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RoboticsProcessor();
        }

        return INSTANCE;
    }

    public void processRootElement(Element element) {
        ActivityDescriptor activityDescriptor = activityAnalysis.analyzeElement(new ElementAnalysisBridge(element));

        if (activityDescriptor != null) {
            try {
                JDefinedClass injectorDefinedClass = injectonGenerator.buildInjector(activityDescriptor);

                activityGenerator.generate(activityDescriptor, injectorDefinedClass);
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
            codeModel.build(new FilerSourceCodeWriter(filer));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }
}
