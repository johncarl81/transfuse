package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.ParcelableAnalysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.ParcelableGenerator;
import org.androidtransfuse.model.ParcelableDescriptor;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Executes the analysis and generation of an annotated @Parcel class.
 *
 * @author John Ericksen
 */
public class ParcelTransactionWorker extends AbstractCompletionTransactionWorker<Provider<ASTType>, JDefinedClass> {

    private final ParcelableAnalysis parcelableAnalysis;
    private final ParcelableGenerator parcelableGenerator;

    @Inject
    public ParcelTransactionWorker(ParcelableAnalysis parcelableAnalysis, ParcelableGenerator parcelableGenerator) {
        this.parcelableAnalysis = parcelableAnalysis;
        this.parcelableGenerator = parcelableGenerator;
    }

    @Override
    public JDefinedClass innerRun(Provider<ASTType> valueProvider) {

        ASTType value = valueProvider.get();

        ParcelableDescriptor analysis = parcelableAnalysis.analyze(value);

        return parcelableGenerator.generateParcelable(value, analysis);
    }
}
