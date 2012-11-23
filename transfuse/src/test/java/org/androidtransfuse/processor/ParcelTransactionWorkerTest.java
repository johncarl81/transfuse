package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.ParcelableAnalysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.ParcelableGenerator;
import org.androidtransfuse.model.ParcelableDescriptor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author John Ericksen
 */
public class ParcelTransactionWorkerTest {

    private ParcelTransactionWorker parcelTransaction;
    private ParcelableAnalysis mockAnalysis;
    private ParcelableGenerator mockGenerator;
    private ParcelableDescriptor mockDescriptor;
    private ASTType input;
    private JDefinedClass output;

    @Before
    public void setup() {
        mockAnalysis = mock(ParcelableAnalysis.class);
        mockGenerator = mock(ParcelableGenerator.class);
        mockDescriptor = mock(ParcelableDescriptor.class);
        input = mock(ASTType.class);
        output = mock(JDefinedClass.class);

        parcelTransaction = new ParcelTransactionWorker(mockAnalysis, mockGenerator);
    }

    @Test
    public void test() {

        when(mockAnalysis.analyze(input)).thenReturn(mockDescriptor);
        when(mockGenerator.generateParcelable(input, mockDescriptor)).thenReturn(output);

        assertFalse(parcelTransaction.isComplete());

        assertEquals(output, parcelTransaction.runScoped(input));

        assertTrue(parcelTransaction.isComplete());
    }
}
