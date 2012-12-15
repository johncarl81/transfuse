package org.androidtransfuse.processor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author John Ericksen
 */
public class TransactionProcessorChainTest {

    private TransactionProcessorChain chain;
    private TransactionProcessor mockProcessor1;
    private TransactionProcessor mockProcessor2;

    @Before
    public void setUp() throws Exception {
        mockProcessor1 = mock(TransactionProcessor.class);
        mockProcessor2 = mock(TransactionProcessor.class);

        chain = new TransactionProcessorChain(mockProcessor1, mockProcessor2);
    }

    @Test
    public void testExecution() {
        when(mockProcessor1.isComplete()).thenReturn(true);
        when(mockProcessor2.isComplete()).thenReturn(false);

        chain.execute();

        verify(mockProcessor1).execute();
        verify(mockProcessor2).execute();
    }

    @Test
    public void testProcessor1Incomplete() {
        when(mockProcessor1.isComplete()).thenReturn(false);
        when(mockProcessor2.isComplete()).thenReturn(false);

        chain.execute();

        verify(mockProcessor1).execute();
        verifyZeroInteractions(mockProcessor2);
    }

    @Test
    public void testProcessor2Complete() {
        when(mockProcessor1.isComplete()).thenReturn(true);
        when(mockProcessor2.isComplete()).thenReturn(true);

        chain.execute();

        verify(mockProcessor1).execute();
        verify(mockProcessor2, times(0)).execute();
    }

    @Test
    public void testCompletionStatus() {
        when(mockProcessor1.isComplete()).thenReturn(true);
        when(mockProcessor2.isComplete()).thenReturn(true);
        assertTrue(chain.isComplete());

        when(mockProcessor1.isComplete()).thenReturn(false);
        assertFalse(chain.isComplete());

        when(mockProcessor1.isComplete()).thenReturn(true);
        when(mockProcessor2.isComplete()).thenReturn(false);
        assertFalse(chain.isComplete());

    }
}
