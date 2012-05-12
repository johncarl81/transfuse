package org.androidtransfuse;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.androidtransfuse.analysis.adapter.ASTElementConverterFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Application;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.config.TransfuseGenerationGuiceModule;
import org.androidtransfuse.gen.FilerSourceCodeWriter;
import org.androidtransfuse.gen.ResourceCodeWriter;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RBuilder;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.model.r.RResourceComposite;
import org.androidtransfuse.processor.ApplicationProcessor;
import org.androidtransfuse.processor.ComponentProcessor;
import org.androidtransfuse.processor.TransfuseAssembler;
import org.androidtransfuse.util.*;

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
@SupportedAnnotationTypes({"org.androidtransfuse.annotations.Activity",
        "org.androidtransfuse.annotations.Application",
        "org.androidtransfuse.annotations.TransfuseModule"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TransfuseAnnotationProcessor extends AbstractProcessor {

    private boolean processorRan = false;
    @Inject
    private TransfuseProcessor transfuseProcessor;
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
            Injector injector = Guice.createInjector(new TransfuseGenerationGuiceModule(new MessagerLogger(processingEnv.getMessager())));
            injector.injectMembers(this);
        } catch (RuntimeException e) {
            logger.error("Error during init of TransfuseAnnotationProcessor", e);
            throw e;
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        if (!processorRan) {

            //setup
            transfuseProcessor.processModule(wrapASTCollection(
                    roundEnvironment.getElementsAnnotatedWith(TransfuseModule.class)
            ));

            File manifestFile = manifestLocatorFactory.buildManifestLocator(processingEnv.getFiler()).findManifest();
            Manifest manifest = manifestParser.readManifest(manifestFile);

            transfuseProcessor.processManifest(manifest);

            RResourceComposite r = new RResourceComposite(
                    buildR(manifest.getApplicationPackage() + ".R"),
                    buildR("android.R")
            );

            transfuseProcessor.processR(r);

            //processing
            ApplicationProcessor applicationProcessor = transfuseProcessor.getApplicationProcessor();

            Collection<? extends ASTType> applicationTypes = wrapASTCollection(roundEnvironment.getElementsAnnotatedWith(Application.class));

            if (applicationTypes.size() > 1) {
                //todo: throw exception
            }

            ComponentProcessor componentProcessor;
            if (applicationTypes.isEmpty()) {
                componentProcessor = applicationProcessor.createComponentProcessor();
            } else {
                componentProcessor = applicationProcessor.processApplication(applicationTypes.iterator().next());
            }

            for (Class<? extends Annotation> annotationClass : Arrays.asList(Activity.class)) {
                componentProcessor.processComponent(wrapASTCollection(
                        roundEnvironment.getElementsAnnotatedWith(annotationClass)
                ));
            }

            //assembling generated code
            TransfuseAssembler transfuseAssembler = applicationProcessor.getTransfuseAssembler();

            Filer filer = processingEnv.getFiler();

            transfuseAssembler.writeSource(new FilerSourceCodeWriter(filer),
                    new ResourceCodeWriter(filer));

            Manifest updatedManifest = transfuseAssembler.buildManifest();

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
