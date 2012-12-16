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

import org.androidtransfuse.config.EnterableScope;

import javax.inject.Provider;

/**
 * Executes the given instance of a TransactionWorker with in a code generation scoped transaction.  A unique instance
 * of CodeModel is supplied in this scope which is used to generate code at the end of the transaction.  If a
 * TransactionRuntimeException is thrown this transaction will effectively reset and allow the TransactionWorker
 * to be retried at a later code generation round.
 *
 * @author John Ericksen
 */
public class ScopedTransactionWorker<V, R> implements TransactionWorker<V, R> {

    private final EnterableScope simpleScope;
    private final Provider<? extends TransactionWorker<V, R>> workerProvider;
    private TransactionWorker<V, R> scoped = null;
    private boolean complete = false;
    private Exception error;

    public ScopedTransactionWorker(EnterableScope simpleScope, Provider<? extends TransactionWorker<V, R>> workerProvider) {
        this.simpleScope = simpleScope;
        this.workerProvider = workerProvider;
    }

    @Override
    public boolean isComplete() {
        return complete && scoped != null && scoped.isComplete();
    }

    @Override
    public R run(V value) {

        try {
            simpleScope.enter();

            scoped = workerProvider.get();
            R result = scoped.run(value);

            complete = true;
            return result;

        } catch (TransactionRuntimeException re) {
            //retry later
            error = re;
            complete = false;
        } finally {
            simpleScope.exit();
        }
        return null;
    }

    public Exception getError() {
        return error;
    }
}
