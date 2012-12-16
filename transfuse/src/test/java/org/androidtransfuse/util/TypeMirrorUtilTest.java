/**
 * Copyright 2012 John Ericksen
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
