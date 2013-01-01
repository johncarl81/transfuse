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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author John Ericksen
 */
public class TransactionTest {

    private Transaction transaction;
    private TransactionWorker transactionWorker;
    private Object input;
    private Object result;

    @Before
    public void setup() {
        input = new Object();
        result = new Object();
        transactionWorker = mock(ScopedTransactionWorker.class);
        transaction = new Transaction(input, transactionWorker);
    }

    @Test
    public void testSuccessfulRun() {
        when(transactionWorker.run(input)).thenReturn(result);

        assertFalse(transaction.isComplete());

        transaction.run();

        when(transactionWorker.isComplete()).thenReturn(true);

        assertTrue(transaction.isComplete());

        assertEquals(result, transaction.getResult());
        assertEquals(input, transaction.getValue());
    }

    @Test
    public void testIncomplete() {
        when(transactionWorker.run(input)).thenReturn(null);

        assertFalse(transaction.isComplete());

        transaction.run();

        when(transactionWorker.isComplete()).thenReturn(false);

        assertFalse(transaction.isComplete());

        assertEquals(null, transaction.getResult());
        assertEquals(input, transaction.getValue());
    }

    @Test
    public void testError() {
        Exception exception = new Exception();

        when(transactionWorker.getError()).thenReturn(exception);

        assertEquals(exception, transaction.getError());
    }
}
