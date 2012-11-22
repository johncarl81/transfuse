package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author John Ericksen
 */
public class ParcelProcessorTest {

    private ParcelProcessor parcelProcessor;
    private TransactionProcessor mockProcessor;
    private ParcelTransactionFactory mockTransactionFactory;
    private ASTType input;
    private Transaction mockTransaction;

    @Before
    public void setup() {
        mockProcessor = mock(TransactionProcessor.class);
        mockTransactionFactory = mock(ParcelTransactionFactory.class);
        input = mock(ASTType.class);
        mockTransaction = mock(Transaction.class);

        parcelProcessor = new ParcelProcessor(mockProcessor, mockTransactionFactory);
    }

    @Test
    public void testSubmit() {

        when(mockTransactionFactory.buildTransaction(input)).thenReturn(mockTransaction);

        parcelProcessor.submit(Collections.singleton(input));

        verify(mockProcessor).submit(mockTransaction);
    }

    @Test
    public void testExecute() {

        parcelProcessor.execute();

        verify(mockProcessor).execute();
    }

    @Test
    public void testCheckForErrorsFailing() {
        testCheckForErrors(true);
    }

    @Test
    public void testCheckForErrorsPassing() {
        testCheckForErrors(false);
    }

    private void testCheckForErrors(boolean errored) {
        try {
            when(mockProcessor.isComplete()).thenReturn(!errored);

            parcelProcessor.checkForErrors();

            // Should not get this far if errored.
            assertFalse(errored);
        } catch (TransfuseAnalysisException exception) {
            // Exception should be thrown if errored.
            assertTrue(errored);
        }
    }
}
