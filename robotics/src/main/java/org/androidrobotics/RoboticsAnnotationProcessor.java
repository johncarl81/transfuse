package org.androidrobotics;

import org.androidrobotics.util.ElementVisitorAdaptor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author John Ericksen
 */
@SupportedAnnotationTypes("org.androidrobotics.annotations.Activity")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
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

            for (Element element : roundEnvironment.getRootElements()) {
                element.accept(new ElementVisitorAdaptor() {
                    @Override
                    public Object visitType(TypeElement typeElement, Object o) {
                        roboticsProcessor.processRootElement(typeElement);
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                }, "test");

            }

            roboticsProcessor.writeSource(processingEnv.getFiler());

            processorRan = true;
            return true;
        }
        return false;
    }
}
