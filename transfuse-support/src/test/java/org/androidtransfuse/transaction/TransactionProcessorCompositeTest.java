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
package org.androidtransfuse.transaction;

import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author John Ericksen
 */
public class TransactionProcessorCompositeTest {

    private TransactionProcessorComposite<Object, Object> processor;
    private ImmutableSet<TransactionProcessor<Object, Object>> mockProcessors;

    @Before
    public void setUp() throws Exception {

        ImmutableSet.Builder<TransactionProcessor<Object, Object>> setBuilder = ImmutableSet.builder();

        for (int i = 0; i < 10; i++) {
            setBuilder.add(Mockito.mock(TransactionProcessor.class));
        }

        mockProcessors = setBuilder.build();
        processor = new TransactionProcessorComposite<Object, Object>(mockProcessors);
    }

    @Test
    public void testExecute() {
        processor.execute();

        for (TransactionProcessor mockProcessor : mockProcessors) {
            Mockito.verify(mockProcessor).execute();
        }
    }

    @Test
    public void testComplete() {
        for (TransactionProcessor mockProcessor : mockProcessors) {
            Mockito.when(mockProcessor.isComplete()).thenReturn(true);
        }

        Assert.assertTrue(processor.isComplete());

        for (TransactionProcessor mockProcessor : mockProcessors) {
            Mockito.verify(mockProcessor).isComplete();
        }
    }

    @Test
    public void testNotComplete() {
        Mockito.when(mockProcessors.iterator().next().isComplete()).thenReturn(false);

        Assert.assertFalse(processor.isComplete());
    }
}
