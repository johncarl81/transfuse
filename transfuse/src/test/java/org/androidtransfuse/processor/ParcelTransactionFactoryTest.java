package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.adapter.ASTEmptyType;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class ParcelTransactionFactoryTest {

    private ParcelTransactionFactory factory;
    private ASTType input;

    @Before
    public void setup() {
        input = new ASTEmptyType("Test");
        factory = new ParcelTransactionFactory();
    }

    @Test
    public void testBuild() {
        assertNotNull(factory.buildTransaction(input));
    }
}
