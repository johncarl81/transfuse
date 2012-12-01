package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.adapter.ASTType;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public interface WorkerProvider extends Provider<TransactionWorker<Provider<ASTType>, Void>> {

}
