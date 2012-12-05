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
