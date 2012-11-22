package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.ParcelsGenerator;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ParcelsTransaction implements TransactionWorker<Map<ASTType, JDefinedClass>, Void> {

    private ParcelsGenerator parcelsGenerator;

    private boolean complete = false;

    @Inject
    public ParcelsTransaction(ParcelsGenerator parcelsGenerator) {
        this.parcelsGenerator = parcelsGenerator;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public Void runScoped(Map<ASTType, JDefinedClass> value) {
        parcelsGenerator.generate(value);
        complete = true;
        return null;
    }
}
