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
package org.androidtransfuse.transaction;

import com.google.common.collect.ImmutableSet;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class TransactionProcessorComposite<V, R> implements TransactionProcessor<V, R> {

    private final ImmutableSet<TransactionProcessor<V, R>> processors;

    public TransactionProcessorComposite(ImmutableSet<TransactionProcessor<V, R>> processors) {
        this.processors = processors;
    }

    @Override
    public void execute() {
        for (TransactionProcessor processor : processors) {
            processor.execute();
        }
    }

    @Override
    public boolean isComplete() {
        for (TransactionProcessor processor : processors) {
            if (!processor.isComplete()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ImmutableSet<Exception> getErrors() {
        ImmutableSet.Builder<Exception> exceptions = ImmutableSet.builder();
        for (TransactionProcessor processor : processors) {
            exceptions.addAll(processor.getErrors());
        }
        return exceptions.build();
    }

    @Override
    public Map<V, R> getResults() {
        Map<V, R> results = new HashMap<V, R>();

        for (TransactionProcessor<V, R> processor : processors) {
            results.putAll(processor.getResults());
        }

        return results;
    }
}
