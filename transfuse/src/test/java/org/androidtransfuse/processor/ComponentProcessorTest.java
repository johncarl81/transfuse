package org.androidtransfuse.processor;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.GeneratorRepository;
import org.androidtransfuse.gen.Generator;
import org.androidtransfuse.util.matcher.AlwaysMatch;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author John Ericksen
 */
public class ComponentProcessorTest {

    @Inject
    private ASTClassFactory astClassFactory;
    private Generator<ASTType> mockGenerator;
    private ComponentProcessor componentProcessor;

    @Before
    public void setUp() throws Exception {
        TransfuseTestInjector.inject(this);

        mockGenerator = mock(Generator.class);

        GeneratorRepository repository = new GeneratorRepository();
        repository.add(new AlwaysMatch(), mockGenerator);
        componentProcessor = new ComponentProcessor(repository);
    }

    @Test
    public void testProcess() {
        ASTType astType = astClassFactory.getType(ComponentProcessorTest.class);
        componentProcessor.process(Collections.singleton(astType));

        verify(mockGenerator).generate(astType);
    }
}
