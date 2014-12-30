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

import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.gen.FilerResourceWriter;
import org.androidtransfuse.gen.FilerSourceCodeWriter;
import org.androidtransfuse.transaction.CodeGenerationScopedTransactionWorker;
import org.androidtransfuse.transaction.TransactionWorker;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class CodeGenerationWrapperProvider<V, R> implements Provider<TransactionWorker<V, R>> {

    private final Provider<JCodeModel> codeModelProvider;
    private final Provider<FilerSourceCodeWriter> sourceCodeWriterProvider;
    private final Provider<FilerResourceWriter> resourceCodeWriterProvider;
    private final Provider<? extends TransactionWorker<V, R>> workerProvider;

    public CodeGenerationWrapperProvider(Provider<? extends TransactionWorker<V, R>> workerProvider,
                                         Provider<JCodeModel> codeModelProvider,
                                         Provider<FilerSourceCodeWriter> sourceCodeWriterProvider,
                                         Provider<FilerResourceWriter> resourceCodeWriterProvider) {
        this.codeModelProvider = codeModelProvider;
        this.sourceCodeWriterProvider = sourceCodeWriterProvider;
        this.resourceCodeWriterProvider = resourceCodeWriterProvider;
        this.workerProvider = workerProvider;
    }

    @Override
    public TransactionWorker<V, R> get() {
        return new CodeGenerationScopedTransactionWorker<V, R>(
                codeModelProvider.get(), sourceCodeWriterProvider.get(), resourceCodeWriterProvider.get(), workerProvider.get());
    }
}
