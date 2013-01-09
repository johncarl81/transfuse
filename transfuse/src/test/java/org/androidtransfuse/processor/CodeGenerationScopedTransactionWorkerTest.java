/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.processor;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JCodeModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * @author John Ericksen
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(JCodeModel.class)
public class CodeGenerationScopedTransactionWorkerTest {

    private CodeGenerationScopedTransactionWorker<Object, Object> worker;
    private JCodeModel mockCodeModel;
    private CodeWriter mockCodeWriter;
    private CodeWriter mockResourceWriter;
    private TransactionWorker<Object, Object> mockWorker;


    @Before
    public void setUp() throws Exception {

        mockCodeModel = mock(JCodeModel.class);
        mockCodeWriter = mock(CodeWriter.class);
        mockResourceWriter = mock(CodeWriter.class);
        mockWorker = mock(TransactionWorker.class);

        worker = new CodeGenerationScopedTransactionWorker<Object, Object>(mockCodeModel, mockCodeWriter, mockResourceWriter, mockWorker);
    }

    @Test
    public void testRun() throws Exception {

        assertFalse(worker.isComplete());

        Object mockValue = mock(Object.class);

        worker.run(mockValue);

        verify(mockWorker).run(mockValue);
        verify(mockCodeModel).build(mockCodeWriter, mockResourceWriter);

        assertTrue(worker.isComplete());
    }
}
