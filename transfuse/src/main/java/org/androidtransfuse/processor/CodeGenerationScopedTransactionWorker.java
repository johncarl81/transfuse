/**
 * Copyright 2012 John Ericksen
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
import org.androidtransfuse.util.TransfuseRuntimeException;

import java.io.IOException;

/**
 * @author John Ericksen
 */
public class CodeGenerationScopedTransactionWorker<V, R> extends AbstractCompletionTransactionWorker<V, R> {

    private final JCodeModel codeModel;
    private final FilerSourceCodeWriter codeWriter;
    private final FilerResourceWriter resourceWriter;
    private final TransactionWorker<V, R> worker;

    public CodeGenerationScopedTransactionWorker(JCodeModel codeModel,
                                                 FilerSourceCodeWriter codeWriter,
                                                 FilerResourceWriter resourceWriter,
                                                 TransactionWorker<V, R> worker) {
        this.codeModel = codeModel;
        this.codeWriter = codeWriter;
        this.resourceWriter = resourceWriter;
        this.worker = worker;
    }

    @Override
    public R innerRun(V value) {
        try {
            R result = worker.run(value);

            codeModel.build(codeWriter, resourceWriter);

            return result;
        } catch (IOException e) {
            throw new TransfuseRuntimeException("Unable to perform code generation", e);
        }
    }
}
