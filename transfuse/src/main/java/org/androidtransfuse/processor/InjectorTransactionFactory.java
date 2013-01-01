/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class InjectorTransactionFactory implements TransactionFactory<Provider<ASTType>, JDefinedClass> {

    private final ScopedTransactionFactory scopedTransactionFactory;
    private final Provider<TransactionWorker<Provider<ASTType>, JDefinedClass>> workerProvider;

    @Inject
    public InjectorTransactionFactory(ScopedTransactionFactory scopedTransactionFactory,
                                      @Named(TransfuseSetupGuiceModule.INJECTOR_TRANSACTION_WORKER)
                                      Provider<TransactionWorker<Provider<ASTType>, JDefinedClass>> workerProvider) {
        this.scopedTransactionFactory = scopedTransactionFactory;
        this.workerProvider = workerProvider;
    }

    public Transaction<Provider<ASTType>, JDefinedClass> buildTransaction(Provider<ASTType> astTypeProvider) {
        return scopedTransactionFactory.buildTransaction(astTypeProvider, workerProvider);
    }
}
