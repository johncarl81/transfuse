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

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

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
        Transaction<String, String> transaction = mock(Transaction.class);
        when(transaction.isComplete()).thenReturn(false);

        pool.submit(transaction);

        pool.execute();

        verify(transaction).run();

        assertFalse(pool.isComplete());
    }

    @Test
    public void testAlreadyCompleteExecute() {
        Transaction<String, String> transaction = mock(Transaction.class);
        when(transaction.isComplete()).thenReturn(true);

        pool.submit(transaction);

        pool.execute();

        verify(transaction, times(0)).run();

        assertTrue(pool.isComplete());
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

        assertEquals(1, results.size());
        assertEquals(TEST_OUTPUT, results.get(TEST_INPUT));
    }
}
