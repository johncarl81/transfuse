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

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.gen.FilerResourceWriter;
import org.androidtransfuse.gen.FilerSourceCodeWriter;
import org.androidtransfuse.transaction.TransactionProcessor;
import org.androidtransfuse.transaction.TransactionProcessorBuilder;
import org.androidtransfuse.transaction.TransactionProcessorPool;
import org.androidtransfuse.transaction.TransactionWorker;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Builds a Transaction supporting the AnalysisGeneration
 *
 * @author John Ericksen
 */
public class AnalysisGenerationTransactionProcessorBuilder implements TransactionProcessorBuilder<Provider<ASTType>, JDefinedClass> {

    private final ScopedTransactionFactory scopedTransactionFactory;
    private final TransactionProcessorPool<Provider<ASTType>, JDefinedClass> transactionProcessor;
    private final CodeGenerationWrapperProvider<Provider<ASTType>, JDefinedClass> workerProvider;

    @Inject
    public AnalysisGenerationTransactionProcessorBuilder(
            /*@Assisted*/ Provider<TransactionWorker<Provider<ASTType>, JDefinedClass>> workerProvider,
            Provider<JCodeModel> codeModelProvider,
            Provider<FilerSourceCodeWriter> sourceCodeWriterProvider,
            Provider<FilerResourceWriter> resourceCodeWriterProvider,
            ScopedTransactionFactory scopedTransactionFactory) {
        this.scopedTransactionFactory = scopedTransactionFactory;
        transactionProcessor = new TransactionProcessorPool<Provider<ASTType>, JDefinedClass>();
        this.workerProvider = new CodeGenerationWrapperProvider<Provider<ASTType>, JDefinedClass>(workerProvider, codeModelProvider, sourceCodeWriterProvider, resourceCodeWriterProvider);
    }

    @Override
    public void submit(Provider<ASTType> astTypeProvider) {
        transactionProcessor.submit(scopedTransactionFactory.buildTransaction(astTypeProvider, workerProvider));
    }

    @Override
    public TransactionProcessor<Provider<ASTType>, JDefinedClass> getTransactionProcessor() {
        return transactionProcessor;
    }
}
