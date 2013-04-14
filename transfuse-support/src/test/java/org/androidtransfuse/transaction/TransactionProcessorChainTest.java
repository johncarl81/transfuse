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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author John Ericksen
 */
public class TransactionProcessorChainTest {

    private TransactionProcessorChain chain;
    private TransactionProcessor mockProcessor1;
    private TransactionProcessor mockProcessor2;

    @Before
    public void setUp() throws Exception {
        mockProcessor1 = Mockito.mock(TransactionProcessor.class);
        mockProcessor2 = Mockito.mock(TransactionProcessor.class);

        chain = new TransactionProcessorChain(mockProcessor1, mockProcessor2);
    }

    @Test
    public void testExecution() {
        Mockito.when(mockProcessor1.isComplete()).thenReturn(true);
        Mockito.when(mockProcessor2.isComplete()).thenReturn(false);

        chain.execute();

        Mockito.verify(mockProcessor1).execute();
        Mockito.verify(mockProcessor2).execute();
    }

    @Test
    public void testProcessor1Incomplete() {
        Mockito.when(mockProcessor1.isComplete()).thenReturn(false);
        Mockito.when(mockProcessor2.isComplete()).thenReturn(false);

        chain.execute();

        Mockito.verify(mockProcessor1).execute();
        Mockito.verifyZeroInteractions(mockProcessor2);
    }

    @Test
    public void testProcessor2Complete() {
        Mockito.when(mockProcessor1.isComplete()).thenReturn(true);
        Mockito.when(mockProcessor2.isComplete()).thenReturn(true);

        chain.execute();

        Mockito.verify(mockProcessor1).execute();
        Mockito.verify(mockProcessor2, Mockito.times(0)).execute();
    }

    @Test
    public void testCompletionStatus() {
        Mockito.when(mockProcessor1.isComplete()).thenReturn(true);
        Mockito.when(mockProcessor2.isComplete()).thenReturn(true);
        Assert.assertTrue(chain.isComplete());

        Mockito.when(mockProcessor1.isComplete()).thenReturn(false);
        Assert.assertFalse(chain.isComplete());

        Mockito.when(mockProcessor1.isComplete()).thenReturn(true);
        Mockito.when(mockProcessor2.isComplete()).thenReturn(false);
        Assert.assertFalse(chain.isComplete());

    }
}
