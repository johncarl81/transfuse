package org.androidrobotics;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author John Ericksen
 */
@SupportedAnnotationTypes("org.androidrobotics.annotations.Activity")
public class RoboticsAnnotationProcessor extends AbstractProcessor {

    private boolean processorRan = false;
    private RoboticsProcessor roboticsProcessor;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        roboticsProcessor = RoboticsProcessor.getInstance();
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        if (!processorRan) {

            System.out.println("Found: " + roundEnvironment);

            for (Element element : roundEnvironment.getRootElements()) {
                roboticsProcessor.processRootElement(element);
            }

            roboticsProcessor.writeSource(processingEnv.getFiler());

            processorRan = true;
            return true;
        }
        return false;
    }
}
