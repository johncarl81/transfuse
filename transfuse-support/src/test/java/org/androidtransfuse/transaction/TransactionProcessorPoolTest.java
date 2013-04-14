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

import java.util.Map;

/**
 * @author John Ericksen
 */
public class TransactionProcessorPoolTest {

    private static final String TEST_INPUT = "input";
    private static final String TEST_OUTPUT = "output";

    private TransactionProcessorPool<String, String> pool;

    @Before
    public void setUp() throws Exception {
        pool = new TransactionProcessorPool<String, String>();
    }

    @Test
    public void testExecute() {
        Transaction<String, String> transaction = Mockito.mock(Transaction.class);
        Mockito.when(transaction.isComplete()).thenReturn(false);

        pool.submit(transaction);

        pool.execute();

        Mockito.verify(transaction).run();

        Assert.assertFalse(pool.isComplete());
    }

    @Test
    public void testAlreadyCompleteExecute() {
        Transaction<String, String> transaction = Mockito.mock(Transaction.class);
        Mockito.when(transaction.isComplete()).thenReturn(true);

        pool.submit(transaction);

        pool.execute();

        Mockito.verify(transaction, Mockito.times(0)).run();

        Assert.assertTrue(pool.isComplete());
    }

    @Test
    public void testResults() {
        Transaction<String, String> transaction = new Transaction<String, String>(TEST_INPUT, new AbstractCompletionTransactionWorker<String, String>() {
            @Override
            public String innerRun(String value) {
                return TEST_OUTPUT;
            }
        });

        pool.submit(transaction);
        pool.execute();

        Map<String, String> results = pool.getResults();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(TEST_OUTPUT, results.get(TEST_INPUT));
    }
}
