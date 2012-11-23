package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTEmptyType;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.config.ThreadLocalScope;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Provider;

import static junit.framework.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * @author John Ericksen
 */
public class ParcelTransactionFactoryTest {

    private ParcelTransactionFactory factory;
    private ASTType input;

    @Before
    public void setup() {
        input = new ASTEmptyType("Test");
        ThreadLocalScope simpleScope = new ThreadLocalScope();
        Provider<TransactionWorker<ASTType, JDefinedClass>> workerProvider = mock(Provider.class);
        factory = new ParcelTransactionFactory(simpleScope, workerProvider);
    }

    @Test
    public void testBuild() {
        assertNotNull(factory.buildTransaction(input));
    }
}
