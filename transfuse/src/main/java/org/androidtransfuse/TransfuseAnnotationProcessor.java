package org.androidtransfuse;

import com.google.inject.Injector;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTElementConverterFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.ApplicationGenerator;
import org.androidtransfuse.gen.CodeWriterFactory;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RBuilder;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.model.r.RResourceComposite;
import org.androidtransfuse.processor.ComponentProcessor;
import org.androidtransfuse.processor.TransfuseAssembler;
import org.androidtransfuse.processor.TransfuseInjector;
import org.androidtransfuse.processor.TransfuseProcessor;
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
import java.util.HashSet;
import java.util.Set;

/**
 * Transfuse Annotation processor.  Kicks off the process of analyzing and generating code based on the compiled
 * codebase.
 *
 * To use this class, you simply have to annotate your classes with the proper root components (Activity,
 * Application, etc) and have this annotation processor on the classpath during a full compilation.
 *
 * This approach is compatible with Java 6 and above.
 *
 * See http://androidtransfuse.org for more details
 *
 * @author John Ericksen
 */
@SupportedAnnotationTypes({"org.androidtransfuse.annotations.Activity",
        "org.androidtransfuse.annotations.Application",
        "org.androidtransfuse.annotations.BroadcastReceiver",
        "org.androidtransfuse.annotations.Service",
        "org.androidtransfuse.annotations.Fragment",
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
        Injector injector = TransfuseInjector.getInstance().buildSetupInjector(processingEnv.getMessager());
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

            Injector injector = TransfuseInjector.getInstance().buildProcessingInjector(r, manifest);

            TransfuseProcessor transfuseProcessor = injector.getInstance(TransfuseProcessor.class);

            transfuseProcessor.processModule(getASTTypesAnnotatedWith(roundEnvironment, TransfuseModule.class));

            //process Application
            ApplicationGenerator applicationProcessor = transfuseProcessor.getApplicationProcessor();

            Collection<? extends ASTType> applicationTypes = getASTTypesAnnotatedWith(roundEnvironment, Application.class);

            if (applicationTypes.size() > 1) {
                throw new TransfuseAnalysisException("Unable to process with more than one application defined");
            }

            if (applicationTypes.isEmpty()) {
                applicationProcessor.generate();
            } else {
                applicationProcessor.generate(applicationTypes.iterator().next());
            }

            ComponentProcessor componentProcessor = applicationProcessor.getComponentProcessor();

            //process components
            Set<ASTType> types = new HashSet<ASTType>();

            types.addAll(getASTTypesAnnotatedWith(roundEnvironment, Activity.class));
            types.addAll(getASTTypesAnnotatedWith(roundEnvironment, BroadcastReceiver.class));
            types.addAll(getASTTypesAnnotatedWith(roundEnvironment, Service.class));
            types.addAll(getASTTypesAnnotatedWith(roundEnvironment, Fragment.class));

            componentProcessor.process(types);

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
        return collectionConverterUtil.transform(elementCollection,
                astElementConverterFactory.buildASTElementConverter(ASTType.class)
        );
    }
}
