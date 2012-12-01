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
}
