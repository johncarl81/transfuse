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
package org.androidtransfuse.gen;

import com.sun.codemodel.JCodeModel;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author John Ericksen
 */
public class FilerSourceCodeWriterTest {

    private static final String TEST_PACKAGE = "org.test";
    private static final String TEST_CLASS = "Tester";

    private FilerSourceCodeWriter codeWriter;
    private Filer mockFiler;
    private JavaFileObject mockFile;
    private OutputStream mockOutputStream;
    private JCodeModel codeModel;

    @Before
    public void setUp() throws Exception {
        mockFiler = mock(Filer.class);
        mockFile = mock(JavaFileObject.class);
        mockOutputStream = mock(OutputStream.class);

        codeWriter = new FilerSourceCodeWriter(mockFiler);
        codeModel = new JCodeModel();
    }

    @Test
    public void testCreateSourceFile() throws IOException {

        when(mockFiler.createSourceFile(TEST_PACKAGE + "." + TEST_CLASS)).thenReturn(mockFile);
        when(mockFile.openOutputStream()).thenReturn(mockOutputStream);

        assertEquals(mockOutputStream, codeWriter.openBinary(codeModel._package(TEST_PACKAGE), TEST_CLASS));

        codeWriter.close();
        verify(mockOutputStream).flush();
        verify(mockOutputStream).close();
    }
}
