package org.androidtransfuse.processor;

import org.androidtransfuse.gen.ParcelsGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * @author John Ericksen
 */
public class ParcelsTransactionWorkerTest {

    private ParcelsTransactionWorker parcelsTransaction;
    private ParcelsGenerator mockGenerator;
    private Map input;

    @Before
    public void setup() {
        mockGenerator = mock(ParcelsGenerator.class);
        input = mock(Map.class);
        parcelsTransaction = new ParcelsTransactionWorker(mockGenerator);
    }

    @Test
    public void testRun() {

        assertFalse(parcelsTransaction.isComplete());

        assertNull(parcelsTransaction.runScoped(input));

        assertTrue(parcelsTransaction.isComplete());
        verify(mockGenerator).generate(input);
    }
}
