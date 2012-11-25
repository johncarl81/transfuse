package org.androidtransfuse;

import com.google.inject.ImplementedBy;
import com.google.inject.Key;
import com.google.inject.name.Names;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTElementConverterFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.config.*;
import org.androidtransfuse.gen.ApplicationGenerator;
import org.androidtransfuse.gen.FilerSourceCodeWriter;
import org.androidtransfuse.gen.ResourceCodeWriter;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RBuilder;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.model.r.RResourceComposite;
import org.androidtransfuse.processor.ComponentProcessor;
import org.androidtransfuse.processor.TransfuseAssembler;
import org.androidtransfuse.processor.TransfuseProcessor;
import org.androidtransfuse.util.Logger;
import org.androidtransfuse.util.ManifestLocator;
import org.androidtransfuse.util.ManifestSerializer;
import org.androidtransfuse.util.SupportedAnnotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.Collections2.transform;

/**
 * Transfuse Annotation processor.  Kicks off the process of analyzing and generating code based on the compiled
 * codebase.
 * <p/>
 * To use this class, you simply have to annotate your classes with the proper root components (Activity,
 * Application, etc) and have this annotation processor on the classpath during a full compilation.
 * <p/>
 * This approach is compatible with Java 6 and above.
 * <p/>
 * See http://androidtransfuse.org for more details
 *
 * @author John Ericksen
 */
@SupportedAnnotations({
        Activity.class,
        Application.class,
        BroadcastReceiver.class,
        Service.class,
        Fragment.class,
        TransfuseModule.class,
        Injector.class,
        ImplementedBy.class})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TransfuseAnnotationProcessor extends AnnotationProcessorBase {

    private boolean processorRan = false;
    @Inject
    private ASTElementConverterFactory astElementConverterFactory;
    @Inject
    private ManifestSerializer manifestParser;
    @Inject
    private RBuilder rBuilder;
    @Inject
    private ManifestLocator manifestLocator;
    @Inject
    private FilerSourceCodeWriter codeWriter;
    @Inject
    private ResourceCodeWriter resourceWriter;
    @Inject
    private Logger logger;
    @Inject
    @Named(TransfuseSetupGuiceModule.CODE_GENERATION_SCOPE)
    private EnterableScope codeGenerationScope;
    @Inject
    @Named(TransfuseGenerateGuiceModule.CONFIGURATION_SCOPE)
    private EnterableScope configurationScope;
    @Inject
    private Provider<TransfuseProcessor> processorProvider;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        TransfuseInjector.getInstance().buildSetupInjector(processingEnv).injectMembers(this);
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        if (!processorRan) {

            long start = System.currentTimeMillis();

            //setup transfuse processor with manifest and R classes
            File manifestFile = manifestLocator.findManifest();
            Manifest manifest = manifestParser.readManifest(manifestFile);

            RResourceComposite r = new RResourceComposite(
                    buildR(rBuilder, manifest.getApplicationPackage() + ".R"),
                    buildR(rBuilder, "android.R"));

            configurationScope.enter();
            codeGenerationScope.enter();

            configurationScope.seed(RResource.class, r);
            configurationScope.seed(Key.get(Manifest.class, Names.named(TransfuseGenerateGuiceModule.ORIGINAL_MANIFEST)), manifest);

            TransfuseProcessor transfuseProcessor = processorProvider.get();

            Set<Element> moduleElements = new HashSet<Element>();
            moduleElements.addAll(roundEnvironment.getElementsAnnotatedWith(TransfuseModule.class));
            moduleElements.add(processingEnv.getElementUtils().getTypeElement(TransfuseAndroidModule.class.getName()));
            transfuseProcessor.processModule(wrapASTCollection(moduleElements));
            transfuseProcessor.processImplementedBy(wrapASTCollection(roundEnvironment.getElementsAnnotatedWith(ImplementedBy.class)));

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
            Set<Element> componentElements = new HashSet<Element>();

            componentElements.addAll(roundEnvironment.getElementsAnnotatedWith(org.androidtransfuse.annotations.Injector.class));
            componentElements.addAll(roundEnvironment.getElementsAnnotatedWith(Activity.class));
            componentElements.addAll(roundEnvironment.getElementsAnnotatedWith(BroadcastReceiver.class));
            componentElements.addAll(roundEnvironment.getElementsAnnotatedWith(Service.class));
            componentElements.addAll(roundEnvironment.getElementsAnnotatedWith(Fragment.class));

            componentProcessor.process(wrapASTCollection(componentElements));

            //assembling generated code
            TransfuseAssembler transfuseAssembler = applicationProcessor.getTransfuseAssembler();

            transfuseAssembler.writeSource(codeWriter, resourceWriter);

            Manifest updatedManifest = transfuseAssembler.buildManifest();

            //write manifest back out, updating from processed classes
            manifestParser.writeManifest(updatedManifest, manifestFile);

            processorRan = true;

            logger.info("Transfuse took " + (System.currentTimeMillis() - start) + "ms to process");

            codeGenerationScope.exit();
            configurationScope.exit();

            return true;
        }
        return false;
    }

    private RResource buildR(RBuilder rBuilder, String className) {
        TypeElement rTypeElement = processingEnv.getElementUtils().getTypeElement(className);
        if (rTypeElement != null) {
            Collection<? extends ASTType> rInnerTypes = wrapASTCollection(ElementFilter.typesIn(rTypeElement.getEnclosedElements()));
            return rBuilder.buildR(rInnerTypes);
        }
        return null;
    }

    private Collection<? extends ASTType> getASTTypesAnnotatedWith(RoundEnvironment roundEnvironment, Class<? extends Annotation> annotation) {
        return wrapASTCollection(roundEnvironment.getElementsAnnotatedWith(annotation));
    }

    private Collection<? extends ASTType> wrapASTCollection(Collection<? extends Element> elementCollection) {
        return transform(elementCollection,
                astElementConverterFactory.buildASTElementConverter(ASTType.class)
        );
    }
}
