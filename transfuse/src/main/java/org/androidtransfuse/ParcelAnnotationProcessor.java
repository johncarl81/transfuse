package org.androidtransfuse;

import com.google.inject.Injector;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.ParcelableAnalysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Parcel;
import org.androidtransfuse.gen.FilerSourceCodeWriter;
import org.androidtransfuse.gen.ParcelableGenerator;
import org.androidtransfuse.gen.ParcelsGenerator;
import org.androidtransfuse.gen.ResourceCodeWriter;
import org.androidtransfuse.model.ParcelableDescriptor;
import org.androidtransfuse.processor.TransfuseInjector;
import org.androidtransfuse.util.SupportedAnnotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.inject.Inject;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
@SupportedAnnotations(Parcel.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ParcelAnnotationProcessor extends AnnotationProcessorBase {

    @Inject
    private FilerSourceCodeWriter codeWriter;
    @Inject
    private ResourceCodeWriter resourceWriter;

    private boolean parcelsGenerated = false;

    private Map<ASTType, JDefinedClass> generated = new HashMap<ASTType, JDefinedClass>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        TransfuseInjector.getInstance().buildSetupInjector(processingEnv).injectMembers(this);
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        Set<? extends Element> parcelElements = roundEnvironment.getElementsAnnotatedWith(Parcel.class);

        if (parcelElements.size() > 0) {

            try {
                Collection<? extends ASTType> parcels = wrapASTCollection(parcelElements);

                JCodeModel codeModel = new JCodeModel();

                Injector injector = TransfuseInjector.getInstance().buildInjector(codeModel);

                ParcelableAnalysis parcelableAnalysis = injector.getInstance(ParcelableAnalysis.class);
                ParcelableGenerator parcelableGenerator = injector.getInstance(ParcelableGenerator.class);

                for (ASTType parcel : parcels) {
                    ParcelableDescriptor descriptor = parcelableAnalysis.analyze(parcel);
                    JDefinedClass parcelDefinedClass = parcelableGenerator.generateParcelable(parcel, descriptor);

                    generated.put(parcel, parcelDefinedClass);
                }

                codeModel.build(codeWriter, resourceWriter);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        } else if (!parcelsGenerated) {

            try {

                JCodeModel codeModel = new JCodeModel();

                Injector injector = TransfuseInjector.getInstance().buildInjector(codeModel);

                injector.getInstance(ParcelsGenerator.class).generate(generated);

                codeModel.build(codeWriter, resourceWriter);

                parcelsGenerated = true;
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return false;
    }
}
