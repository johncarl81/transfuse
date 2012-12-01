package org.androidtransfuse;

import org.androidtransfuse.annotations.Parcel;
import org.androidtransfuse.processor.ParcelProcessor;
import org.androidtransfuse.processor.ReloadableASTElementFactory;
import org.androidtransfuse.util.SupportedAnnotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.inject.Inject;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static org.androidtransfuse.config.TransfuseInjector.buildInjector;

/**
 * Annotation processor which generates for classes annotated with @Parcel, Android Parcelable wrapper classes.
 * <p/>
 * In addition this processor will generate the org.androidtransfuse.Parcels class.  This utility defines a mapping
 * of annotated @Parcel class with the Parcelable wrapper and allows for wasy wrapping of any processed @Parcel.
 *
 * @author John Ericksen
 */
@SupportedAnnotations(Parcel.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ParcelAnnotationProcessor extends AnnotationProcessorBase {

    @Inject
    private ParcelProcessor parcelProcessor;
    @Inject
    private ReloadableASTElementFactory reloadableASTElementFactory;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        buildInjector(processingEnv).injectMembers(this);
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        parcelProcessor.submit(reloadableASTElementFactory.buildProviders(roundEnvironment.getElementsAnnotatedWith(Parcel.class)));

        parcelProcessor.execute();

        if (roundEnvironment.processingOver()) {
            // Throws an exception if errors still exist.
            parcelProcessor.checkForErrors();
        }

        return true;
    }
}
