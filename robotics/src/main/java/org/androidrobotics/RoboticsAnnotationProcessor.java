package org.androidrobotics;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.androidrobotics.analysis.adapter.ASTElementConverterFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Application;
import org.androidrobotics.annotations.RoboticsModule;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.gen.FilerSourceCodeWriter;
import org.androidrobotics.gen.ResourceCodeWriter;
import org.androidrobotics.model.manifest.Manifest;
import org.androidrobotics.model.r.RBuilder;
import org.androidrobotics.model.r.RResource;
import org.androidrobotics.model.r.RResourceComposite;
import org.androidrobotics.processor.ApplicationProcessor;
import org.androidrobotics.processor.ComponentProcessor;
import org.androidrobotics.processor.RoboticsAssembler;
import org.androidrobotics.util.*;

import javax.annotation.processing.*;
import javax.inject.Inject;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * @author John Ericksen
 */
@SupportedAnnotationTypes({"org.androidrobotics.annotations.Activity",
        "org.androidrobotics.annotations.Application",
        "org.androidrobotics.annotations.RoboticsModule"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class RoboticsAnnotationProcessor extends AbstractProcessor {

    private boolean processorRan = false;
    @Inject
    private RoboticsProcessor roboticsProcessor;
    @Inject
    private CollectionConverterUtil collectionConverterUtil;
    @Inject
    private ASTElementConverterFactory astElementConverterFactory;
    @Inject
    private Logger logger;
    @Inject
    private ManifestSerializer manifestParser;
    @Inject
    private RBuilder rBuilder;
    @Inject
    private ManifestLocatorFactory manifestLocatorFactory;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        try {
            Injector injector = Guice.createInjector(new RoboticsGenerationGuiceModule(new MessagerLogger(processingEnv.getMessager())));
            injector.injectMembers(this);
        } catch (RuntimeException e) {
            logger.error("Error during init of RoboticsAnnotationProcessor", e);
            throw e;
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        if (!processorRan) {

            roboticsProcessor.processModule(wrapASTCollection(
                    roundEnvironment.getElementsAnnotatedWith(RoboticsModule.class)
            ));

            File manifestFile = manifestLocatorFactory.buildManifestLocator(processingEnv.getFiler()).findManifest();
            Manifest manifest = manifestParser.readManifest(manifestFile);

            roboticsProcessor.processManifest(manifest);

            RResourceComposite r = new RResourceComposite(
                    buildR(manifest.getApplicationPackage() + ".R"),
                    buildR("android.R")
            );

            roboticsProcessor.processR(r);

            ApplicationProcessor applicationProcessor = roboticsProcessor.getApplicationProcessor();

            ComponentProcessor componentProcessor = applicationProcessor.processApplication(wrapASTCollection(
                    roundEnvironment.getElementsAnnotatedWith(Application.class)
            ));

            for (Class<? extends Annotation> annotationClass : Arrays.asList(Activity.class)) {
                componentProcessor.processComponent(wrapASTCollection(
                        roundEnvironment.getElementsAnnotatedWith(annotationClass)
                ));
            }

            RoboticsAssembler roboticsAssembler = componentProcessor.getRoboticsAssembler();

            Filer filer = processingEnv.getFiler();

            roboticsAssembler.writeSource(new FilerSourceCodeWriter(filer),
                    new ResourceCodeWriter(filer));

            Manifest updatedManifest = roboticsAssembler.getManifest();

            //write back out, updating from processed classes
            manifestParser.writeManifest(updatedManifest, manifestFile);

            processorRan = true;
            return true;
        }
        return false;
    }

    private RResource buildR(String className) {
        TypeElement rTypeElement = processingEnv.getElementUtils().getTypeElement(className);
        Collection<? extends ASTType> rInnerTypes = wrapASTCollection(ElementFilter.typesIn(rTypeElement.getEnclosedElements()));

        return rBuilder.buildR(rInnerTypes);
    }

    private Collection<? extends ASTType> wrapASTCollection(Collection<? extends Element> elementCollection) {
        return collectionConverterUtil.wrapCollection(elementCollection,
                astElementConverterFactory.buildASTElementConverter(ASTType.class)
        );
    }
}
