/**
 * Copyright 2011-2015 John Ericksen
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
package org.androidtransfuse.transaction;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JCodeModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author John Ericksen
 */
@RunWith(MockitoJUnitRunner.class)
public class CodeGenerationScopedTransactionWorkerTest {

    private CodeGenerationScopedTransactionWorker<Object, Object> worker;
    private JCodeModel mockCodeModel;
    private CodeWriter mockCodeWriter;
    private CodeWriter mockResourceWriter;
    private TransactionWorker<Object, Object> mockWorker;


    @Before
    public void setUp() throws Exception {

        mockCodeModel = Mockito.mock(JCodeModel.class);
        mockCodeWriter = Mockito.mock(CodeWriter.class);
        mockResourceWriter = Mockito.mock(CodeWriter.class);
        mockWorker = Mockito.mock(TransactionWorker.class);

        worker = new CodeGenerationScopedTransactionWorker<Object, Object>(mockCodeModel, mockCodeWriter, mockResourceWriter, mockWorker);
    }

    @Test
    public void testRun() throws Exception {

        Assert.assertFalse(worker.isComplete());

        Object mockValue = Mockito.mock(Object.class);

        worker.run(mockValue);

        Mockito.verify(mockWorker).run(mockValue);
        Mockito.verify(mockCodeModel).build(mockCodeWriter, mockResourceWriter);

        Assert.assertTrue(worker.isComplete());
    }
}
