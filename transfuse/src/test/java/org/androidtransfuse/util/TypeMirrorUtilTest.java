package org.androidtransfuse.util;

import org.junit.Before;
import org.junit.Test;

import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author John Ericksen
 */
public class TypeMirrorUtilTest {

    private Runnable mockRunnable;
    private MirroredTypeException mockException;
    private TypeMirror mockTypeMirror;

    @Before
    public void setup(){

        mockRunnable = mock(Runnable.class);
        mockException = mock(MirroredTypeException.class);
        mockTypeMirror = mock(TypeMirror.class);
    }

    @Test
    public void testThrows(){
        doThrow(mockException).when(mockRunnable).run();
        when(mockException.getTypeMirror()).thenReturn(mockTypeMirror);

        TypeMirror typeMirror = getTypeMirror(mockRunnable);
        assertEquals(mockTypeMirror, typeMirror);
    }
}
