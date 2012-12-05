package org.androidtransfuse.gen;

import com.sun.codemodel.JCodeModel;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author John Ericksen
 */
public class FilerResourceWriterTest {

    private static final String TEST_PACKAGE = "org.test";
    private static final String TEST_FILENAME = "Tester";

    private FilerResourceWriter resourceWriter;
    private Filer mockFiler;
    private FileObject mockFile;
    private OutputStream mockOutputStream;
    private JCodeModel codeModel;

    @Before
    public void setUp() throws Exception {
        mockFiler = mock(Filer.class);
        mockFile = mock(FileObject.class);
        mockOutputStream = mock(OutputStream.class);

        resourceWriter = new FilerResourceWriter(mockFiler);
        codeModel = new JCodeModel();
    }

    @Test
    public void testCreateResource() throws IOException {

        when(mockFiler.createResource(StandardLocation.SOURCE_OUTPUT, TEST_PACKAGE, TEST_FILENAME)).thenReturn(mockFile);
        when(mockFile.openOutputStream()).thenReturn(mockOutputStream);

        assertEquals(mockOutputStream, resourceWriter.openBinary(codeModel._package(TEST_PACKAGE), TEST_FILENAME));

        resourceWriter.close();
        verify(mockOutputStream).flush();
        verify(mockOutputStream).close();
    }
}
