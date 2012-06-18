package org.androidtransfuse.util;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import static org.junit.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class TypeMirrorUtilTest {

    private TypeMirrorUtil typeMirrorUtil;
    private Runnable mockRunnable;
    private MirroredTypeException mockException;
    private TypeMirror mockTypeMirror;

    @Before
    public void setup(){

        mockRunnable = EasyMock.createMock(Runnable.class);
        mockException = EasyMock.createMock(MirroredTypeException.class);
        mockTypeMirror = EasyMock.createMock(TypeMirror.class);

        typeMirrorUtil = new TypeMirrorUtil();
    }

    @Test
    public void testThrows(){
        EasyMock.reset(mockRunnable, mockException);

        mockRunnable.run();
        EasyMock.expectLastCall().andThrow(mockException);
        EasyMock.expect(mockException.getTypeMirror()).andReturn(mockTypeMirror);

        EasyMock.replay(mockRunnable, mockException);

        TypeMirror typeMirror = typeMirrorUtil.getTypeMirror(mockRunnable);
        assertEquals(mockTypeMirror, typeMirror);

        EasyMock.verify(mockRunnable, mockException);
    }
}
