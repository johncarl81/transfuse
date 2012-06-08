package org.androidtransfuse;

import com.google.inject.Injector;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTElementConverterFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Application;
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.gen.CodeWriterFactory;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RBuilder;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.model.r.RResourceComposite;
import org.androidtransfuse.processor.*;
import org.androidtransfuse.util.CollectionConverterUtil;
import org.androidtransfuse.util.ManifestLocatorFactory;
import org.androidtransfuse.util.ManifestSerializer;

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
    private ManifestSerializer manifestParser;
    @Inject
    private RBuilder rBuilder;
    @Inject
    private ManifestLocatorFactory manifestLocatorFactory;
    @Inject
    private CodeWriterFactory codeWriterFactory;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Injector injector = TransfuseInjector.buildSetupInjector(processingEnv.getMessager());
        injector.injectMembers(this);
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        if (!processorRan) {

            //setup transfuse processor with manifest and R classes
            File manifestFile = manifestLocatorFactory.buildManifestLocator(processingEnv.getFiler()).findManifest();
            Manifest manifest = manifestParser.readManifest(manifestFile);

            RResourceComposite r = new RResourceComposite(
                    buildR(rBuilder, manifest.getApplicationPackage() + ".R"),
                    buildR(rBuilder, "android.R"));

            Injector injector = TransfuseInjector.buildProcessingInjector(r, manifest);

            TransfuseProcessor transfuseProcessor = injector.getInstance(TransfuseProcessor.class);

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

            //process components
            componentProcessor.process(getASTTypesAnnotatedWith(roundEnvironment, Activity.class));
            componentProcessor.process(getASTTypesAnnotatedWith(roundEnvironment, BroadcastReceiver.class));

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

    private RResource buildR(RBuilder rBuilder, String className) {
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
