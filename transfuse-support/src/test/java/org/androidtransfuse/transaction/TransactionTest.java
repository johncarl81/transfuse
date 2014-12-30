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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
        transactionWorker = Mockito.mock(ScopedTransactionWorker.class);
        transaction = new Transaction(input, transactionWorker);
    }

    @Test
    public void testSuccessfulRun() {
        Mockito.when(transactionWorker.run(input)).thenReturn(result);

        Assert.assertFalse(transaction.isComplete());

        transaction.run();

        Mockito.when(transactionWorker.isComplete()).thenReturn(true);

        Assert.assertTrue(transaction.isComplete());

        Assert.assertEquals(result, transaction.getResult());
        Assert.assertEquals(input, transaction.getValue());
    }

    @Test
    public void testIncomplete() {
        Mockito.when(transactionWorker.run(input)).thenReturn(null);

        Assert.assertFalse(transaction.isComplete());

        transaction.run();

        Mockito.when(transactionWorker.isComplete()).thenReturn(false);

        Assert.assertFalse(transaction.isComplete());

        Assert.assertEquals(null, transaction.getResult());
        Assert.assertEquals(input, transaction.getValue());
    }

    @Test
    public void testError() {
        Exception exception = new Exception();

        Mockito.when(transactionWorker.getError()).thenReturn(exception);

        Assert.assertEquals(exception, transaction.getError());
    }
}
