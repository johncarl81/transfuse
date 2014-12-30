/**
 * Copyright 2011-2015 John Ericksen
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
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.transaction.TransactionFactory;
import org.androidtransfuse.transaction.TransactionProcessor;
import org.androidtransfuse.transaction.TransactionProcessorBuilder;
import org.androidtransfuse.transaction.TransactionProcessorPool;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class FactoryProcessor implements TransactionProcessorBuilder<Provider<ASTType>, JDefinedClass> {

    private final TransactionProcessor processor;
    private final TransactionProcessorPool<Provider<ASTType>, JDefinedClass> factoryProcessor;
    private final TransactionFactory<Provider<ASTType>, JDefinedClass> factoryTransactionFactory;

    public FactoryProcessor(TransactionProcessor processor,
                            TransactionProcessorPool<Provider<ASTType>, JDefinedClass> factoryProcessor,
                            TransactionFactory<Provider<ASTType>, JDefinedClass> factoryTransactionFactory) {
        this.processor = processor;
        this.factoryProcessor = factoryProcessor;
        this.factoryTransactionFactory = factoryTransactionFactory;
    }

    public void submit(Provider<ASTType> parcel) {
        factoryProcessor.submit(factoryTransactionFactory.buildTransaction(parcel));
    }

    public void execute() {
        processor.execute();
    }

    public TransactionProcessor<Provider<ASTType>, JDefinedClass> getTransactionProcessor() {
        return processor;
    }
}
