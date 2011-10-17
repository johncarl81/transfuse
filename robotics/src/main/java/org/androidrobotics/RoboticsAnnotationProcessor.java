package org.androidrobotics;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.androidrobotics.analysis.adapter.ASTElementConverterFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.RoboticsModule;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.gen.FilerSourceCodeWriter;
import org.androidrobotics.gen.ResourceCodeWriter;
import org.androidrobotics.util.CollectionConverterUtil;
import org.androidrobotics.util.Logger;
import org.androidrobotics.util.MessagerLogger;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * @author John Ericksen
 */
@SupportedAnnotationTypes({"org.androidrobotics.annotations.Activity",
        "org.androidrobotics.annotations.RoboticsModule"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class RoboticsAnnotationProcessor extends AbstractProcessor {

    private boolean processorRan = false;
    private RoboticsProcessor roboticsProcessor;
    private CollectionConverterUtil collectionConverterUtil;
    private ASTElementConverterFactory astElementConverterFactory;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        try {
            Logger logger = new MessagerLogger(processingEnv.getMessager());
            Injector injector = Guice.createInjector(new RoboticsGenerationGuiceModule(logger));
            roboticsProcessor = injector.getInstance(RoboticsProcessor.class);
            collectionConverterUtil = injector.getInstance(CollectionConverterUtil.class);
            astElementConverterFactory = injector.getInstance(ASTElementConverterFactory.class);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        if (!processorRan) {

            roboticsProcessor.processModuleElements(wrapASTCollection(
                    roundEnvironment.getElementsAnnotatedWith(RoboticsModule.class)
            ));


            for (Class<? extends Annotation> annotationClass : Arrays.asList(Activity.class)) {
                roboticsProcessor.processRootElement(wrapASTCollection(
                        roundEnvironment.getElementsAnnotatedWith(annotationClass)
                ));
            }

            roboticsProcessor.verify();

            Filer filer = processingEnv.getFiler();

            roboticsProcessor.writeSource(new FilerSourceCodeWriter(filer),
                    new ResourceCodeWriter(filer));

            processorRan = true;
            return true;
        }
        return false;
    }

    private Collection<? extends ASTType> wrapASTCollection(Set<? extends Element> elementCollection) {
        return collectionConverterUtil.wrapCollection(elementCollection,
                astElementConverterFactory.buildASTElementConverter(ASTType.class)
        );
    }
}
