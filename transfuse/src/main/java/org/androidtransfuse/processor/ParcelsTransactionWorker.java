package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.ParcelsGenerator;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;

/**
 * Executes the generation of the Parcels utility class in a Transaction Worker.
 *
 * @author John Ericksen
 */
public class ParcelsTransactionWorker implements TransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> {

    private ParcelsGenerator parcelsGenerator;

    private boolean complete = false;

    @Inject
    public ParcelsTransactionWorker(ParcelsGenerator parcelsGenerator) {
        this.parcelsGenerator = parcelsGenerator;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public Void runScoped(Map<Provider<ASTType>, JDefinedClass> value) {
        parcelsGenerator.generate(value);
        complete = true;
        return null;
    }
}
