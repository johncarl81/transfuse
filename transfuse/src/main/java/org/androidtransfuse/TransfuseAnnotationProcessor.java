package org.androidtransfuse;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTElementConverterFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Application;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.config.TransfuseGenerationGuiceModule;
import org.androidtransfuse.gen.CodeWriterFactory;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RBuilder;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.model.r.RResourceComposite;
import org.androidtransfuse.processor.*;
import org.androidtransfuse.util.*;

import javax.annotation.processing.*;
import javax.inject.Inject;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.io.File;
import java.lang.annotation.Annotation;
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
    @Inject
    private ProcessorFactory processorFactory;
    @Inject
    private CodeWriterFactory codeWriterFactory;

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

            //setup transfuse processor with manifest and R classes
            File manifestFile = manifestLocatorFactory.buildManifestLocator(processingEnv.getFiler()).findManifest();
            Manifest manifest = manifestParser.readManifest(manifestFile);

            RResourceComposite r = new RResourceComposite(
                    buildR(manifest.getApplicationPackage() + ".R"),
                    buildR("android.R"));

            TransfuseProcessor transfuseProcessor = processorFactory.buildProcessor(r, manifest);

            transfuseProcessor.processModule(getASTTypesAnnotatedWith(roundEnvironment, TransfuseModule.class));

            //process Application
            ApplicationProcessor applicationProcessor = transfuseProcessor.getApplicationProcessor();

            Collection<? extends ASTType> applicationTypes = getASTTypesAnnotatedWith(roundEnvironment, Application.class);

            if (applicationTypes.size() > 1) {
                throw new TransfuseAnalysisException("Unable to process with more than one application defined");
            }

            ComponentProcessor componentProcessor;
            if (applicationTypes.isEmpty()) {
                componentProcessor = applicationProcessor.createComponentProcessor();
            } else {
                componentProcessor = applicationProcessor.processApplication(applicationTypes.iterator().next());
            }

            //process all components
            componentProcessor.processComponent(getASTTypesAnnotatedWith(roundEnvironment, Activity.class));

            //assembling generated code
            TransfuseAssembler transfuseAssembler = applicationProcessor.getTransfuseAssembler();

            Filer filer = processingEnv.getFiler();

            transfuseAssembler.writeSource(codeWriterFactory.buildSourceWriter(filer), codeWriterFactory.buildResourceWriter(filer));

            Manifest updatedManifest = transfuseAssembler.buildManifest();

            //write manifest back out, updating from processed classes
            manifestParser.writeManifest(updatedManifest, manifestFile);

            processorRan = true;
            return true;
        }
        return false;
    }

    private Collection<? extends ASTType> getASTTypesAnnotatedWith(RoundEnvironment roundEnvironment, Class<? extends Annotation> annotation) {
        return wrapASTCollection(roundEnvironment.getElementsAnnotatedWith(annotation));
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
