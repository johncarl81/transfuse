package org.androidtransfuse.util;

import javax.inject.Singleton;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

/**
 * @author John Ericksen
 */
@Singleton
public class TypeMirrorUtil {

    public TypeMirror getTypeMirror(Runnable runnable) {
        //http://blog.retep.org/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor/
        try {
            runnable.run();
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null;
    }
}
