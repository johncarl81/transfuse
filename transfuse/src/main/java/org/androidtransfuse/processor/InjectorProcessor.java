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
import org.androidtransfuse.adapter.ASTType;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class InjectorProcessor implements TransactionProcessorBuilder<Provider<ASTType>, JDefinedClass> {

    private final TransactionProcessor processor;
    private final TransactionProcessorPool<Provider<ASTType>, JDefinedClass> injectorProcessor;
    private final TransactionFactory<Provider<ASTType>, JDefinedClass> injectorTransactionFactory;

    public InjectorProcessor(TransactionProcessor processor,
                             TransactionProcessorPool<Provider<ASTType>, JDefinedClass> injectorProcessor,
                             TransactionFactory<Provider<ASTType>, JDefinedClass> injectorTransactionFactory) {
        this.processor = processor;
        this.injectorProcessor = injectorProcessor;
        this.injectorTransactionFactory = injectorTransactionFactory;
    }

    public void submit(Provider<ASTType> parcel) {
        injectorProcessor.submit(injectorTransactionFactory.buildTransaction(parcel));
    }

    public void execute() {
        processor.execute();
    }

    public TransactionProcessor<Provider<ASTType>, JDefinedClass> getTransactionProcessor() {
        return processor;
    }
}
