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
package org.androidtransfuse.util;

import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Scab class to access the Class paramters of annotations.
 *
 * http://blog.retep.org/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor/
 *
 * @author John Ericksen
 */
public final class TypeMirrorUtil {

    private TypeMirrorUtil(){
        //noop utility class constructor
    }

    public static TypeMirror getTypeMirror(Object annotation, String parameter){
        try {
            annotation.getClass().getMethod(parameter).invoke(annotation);
        } catch (MirroredTypeException mte) {

        } catch (InvocationTargetException invocationException) {
            if(invocationException.getCause() instanceof MirroredTypeException){
                return ((MirroredTypeException)invocationException.getCause()).getTypeMirror();
            }
            throw new TransfuseRuntimeException("Error invoking annotation parameter", invocationException);
        } catch (IllegalAccessException e) {
            throw new TransfuseRuntimeException("Error invoking annotation parameter", e);
        } catch (NoSuchMethodException e) {
            throw new TransfuseRuntimeException("Error invoking annotation parameter", e);
        }
        return null;
    }

    public static List<? extends TypeMirror> getTypeMirrors(Object annotation, String parameter){
        try {
            annotation.getClass().getMethod(parameter).invoke(annotation);
        } catch (MirroredTypeException mte) {

        } catch (InvocationTargetException invocationException) {
            if(invocationException.getCause() instanceof MirroredTypesException){
                return ((MirroredTypesException)invocationException.getCause()).getTypeMirrors();
            }
            throw new TransfuseRuntimeException("Error invoking annotation parameter", invocationException);
        } catch (IllegalAccessException e) {
            throw new TransfuseRuntimeException("Error invoking annotation parameter", e);
        } catch (NoSuchMethodException e) {
            throw new TransfuseRuntimeException("Error invoking annotation parameter", e);
        }
        return null;
    }
}
