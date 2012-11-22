package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class ParcelTransactionFactory {

    public Transaction<ASTType, JDefinedClass> buildTransaction(ASTType parcel) {
        return new ScopedTransaction<ParcelTransaction, ASTType, JDefinedClass>(parcel,
                new ScopedTransactionWorker<ParcelTransaction, ASTType, JDefinedClass>(ParcelTransaction.class));
    }
}
