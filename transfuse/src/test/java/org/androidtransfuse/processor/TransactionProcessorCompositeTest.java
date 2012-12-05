package org.androidtransfuse.processor;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author John Ericksen
 */
public class TransactionProcessorCompositeTest {

    private TransactionProcessorComposite processor;
    private ImmutableSet<TransactionProcessor> mockProcessors;

    @Before
    public void setUp() throws Exception {

        ImmutableSet.Builder<TransactionProcessor> setBuilder = ImmutableSet.builder();

        for (int i = 0; i < 10; i++) {
            setBuilder.add(mock(TransactionProcessor.class));
        }

        mockProcessors = setBuilder.build();
        processor = new TransactionProcessorComposite(mockProcessors);
    }

    @Test
    public void testExecute() {
        processor.execute();

        for (TransactionProcessor mockProcessor : mockProcessors) {
            verify(mockProcessor).execute();
        }
    }

    @Test
    public void testComplete() {
        for (TransactionProcessor mockProcessor : mockProcessors) {
            when(mockProcessor.isComplete()).thenReturn(true);
        }

        assertTrue(processor.isComplete());

        for (TransactionProcessor mockProcessor : mockProcessors) {
            verify(mockProcessor).isComplete();
        }
    }

    @Test
    public void testNotComplete() {
        when(mockProcessors.iterator().next().isComplete()).thenReturn(false);

        assertFalse(processor.isComplete());
    }
}
