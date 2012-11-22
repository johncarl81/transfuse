package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.ParcelableAnalysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.ParcelableGenerator;
import org.androidtransfuse.model.ParcelableDescriptor;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ParcelTransaction implements TransactionWorker<ASTType, JDefinedClass> {

    private final ParcelableAnalysis parcelableAnalysis;
    private final ParcelableGenerator parcelableGenerator;
    private boolean complete = false;

    @Inject
    public ParcelTransaction(ParcelableAnalysis parcelableAnalysis, ParcelableGenerator parcelableGenerator) {
        this.parcelableAnalysis = parcelableAnalysis;
        this.parcelableGenerator = parcelableGenerator;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public JDefinedClass runScoped(ASTType value) {
        ParcelableDescriptor analysis = parcelableAnalysis.analyze(value);
        JDefinedClass definedClass = parcelableGenerator.generateParcelable(value, analysis);

        complete = true;

        return definedClass;
    }
}
