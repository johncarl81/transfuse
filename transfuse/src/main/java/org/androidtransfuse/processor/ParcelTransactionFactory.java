package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class ParcelTransactionFactory {

    public Transaction<ASTType, JDefinedClass> buildTransaction(ASTType parcel) {
        return new ScopedTransaction<ParcelTransactionWorker, ASTType, JDefinedClass>(parcel,
                new ScopedTransactionWorker<ParcelTransactionWorker, ASTType, JDefinedClass>(ParcelTransactionWorker.class));
    }
}
