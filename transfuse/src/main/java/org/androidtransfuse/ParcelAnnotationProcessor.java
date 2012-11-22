package org.androidtransfuse;

import org.androidtransfuse.annotations.Parcel;
import org.androidtransfuse.config.TransfuseInjector;
import org.androidtransfuse.processor.ParcelProcessor;
import org.androidtransfuse.util.SupportedAnnotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.inject.Inject;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author John Ericksen
 */
@SupportedAnnotations(Parcel.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ParcelAnnotationProcessor extends AnnotationProcessorBase {

    @Inject
    private ParcelProcessor parcelProcessor;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        TransfuseInjector.getInstance().buildSetupInjector(processingEnv).injectMembers(this);
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        parcelProcessor.submit(wrapASTCollection(roundEnvironment.getElementsAnnotatedWith(Parcel.class)));

        parcelProcessor.execute();

        if (roundEnvironment.processingOver()) {
            // Throws an exception if errors still exist.
            parcelProcessor.checkForErrors();
        }

        return true;
    }
}
