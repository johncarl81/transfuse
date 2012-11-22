package org.androidtransfuse.processor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author John Ericksen
 */
public class ScopedTransactionTest {

    private ScopedTransaction scopedTransaction;
    private ScopedTransactionWorker scopedTransactionWorker;
    private Object input;
    private Object result;

    @Before
    public void setup() {
        input = new Object();
        result = new Object();
        scopedTransactionWorker = mock(ScopedTransactionWorker.class);
        scopedTransaction = new ScopedTransaction(input, scopedTransactionWorker);
    }

    @Test
    public void testSuccessfulRun() {
        when(scopedTransactionWorker.runScoped(input)).thenReturn(result);

        assertFalse(scopedTransaction.isComplete());

        scopedTransaction.run();

        when(scopedTransactionWorker.isComplete()).thenReturn(true);

        assertTrue(scopedTransaction.isComplete());

        assertEquals(result, scopedTransaction.getResult());
        assertEquals(input, scopedTransaction.getValue());
    }

    @Test
    public void testIncomplete() {
        when(scopedTransactionWorker.runScoped(input)).thenReturn(null);

        assertFalse(scopedTransaction.isComplete());

        scopedTransaction.run();

        when(scopedTransactionWorker.isComplete()).thenReturn(false);

        assertFalse(scopedTransaction.isComplete());

        assertEquals(null, scopedTransaction.getResult());
        assertEquals(input, scopedTransaction.getValue());
    }
}
