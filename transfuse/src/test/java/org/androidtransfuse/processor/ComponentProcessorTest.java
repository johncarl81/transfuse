package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.GeneratorRepository;
import org.androidtransfuse.annotations.Injector;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Provider;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author John Ericksen
 */
public class ComponentProcessorTest {

    private TransactionProcessorBuilder mockGenerator;
    private ComponentProcessor componentProcessor;

    @Before
    public void setUp() {
        mockGenerator = mock(TransactionProcessorBuilder.class);

        GeneratorRepository repository = new GeneratorRepository();
        repository.add(Injector.class, mockGenerator);
        componentProcessor = new ComponentProcessor(repository);
    }

    @Test
    public void testProcess() {
        Provider<ASTType> astTypeProvider = mock(Provider.class);
        componentProcessor.submit(Injector.class, Collections.singleton(astTypeProvider));

        verify(mockGenerator).submit(astTypeProvider);
    }
}
