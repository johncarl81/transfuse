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

/**
 * Base class implementing basic completion status.  If the execution of innerRun() does not throw a runtime exception
 * the completion status is set to true.
 *
 * @author John Ericksen
 */
public abstract class AbstractCompletionTransactionWorker<V, R> implements TransactionWorker<V, R> {

    private boolean complete = false;

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public R run(V value) {
        R result = innerRun(value);

        complete = true;
        return result;
    }

    public abstract R innerRun(V value);

    @Override
    public Exception getError() {
        return null;
    }
}
