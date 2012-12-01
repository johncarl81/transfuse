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
public class ParcelTransactionWorker implements TransactionWorker<Provider<ASTType>, JDefinedClass> {

    private final ParcelableAnalysis parcelableAnalysis;
    private final ParcelableGenerator parcelableGenerator;
    private boolean complete = false;

    @Inject
    public ParcelTransactionWorker(ParcelableAnalysis parcelableAnalysis, ParcelableGenerator parcelableGenerator) {
        this.parcelableAnalysis = parcelableAnalysis;
        this.parcelableGenerator = parcelableGenerator;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public JDefinedClass run(Provider<ASTType> valueProvider) {

        ASTType value = valueProvider.get();

        ParcelableDescriptor analysis = parcelableAnalysis.analyze(value);
        JDefinedClass definedClass = parcelableGenerator.generateParcelable(value, analysis);

        complete = true;

        return definedClass;
    }

    @Override
    public Exception getError() {
        return null;
    }
}
