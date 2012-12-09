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
public class ParcelsTransactionWorker extends AbstractCompletionTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> {

    private ParcelsGenerator parcelsGenerator;

    @Inject
    public ParcelsTransactionWorker(ParcelsGenerator parcelsGenerator) {
        this.parcelsGenerator = parcelsGenerator;
    }

    @Override
    public Void innerRun(Map<Provider<ASTType>, JDefinedClass> value) {
        parcelsGenerator.generate(value);
        return null;
    }
}
