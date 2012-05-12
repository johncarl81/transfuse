package org.androidtransfuse.util;

import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

/**
 * @author John Ericksen
 */
public final class TypeMirrorUtil {

    private static final TypeMirrorUtil INSTANCE = new TypeMirrorUtil();

    public static TypeMirrorUtil getInstance() {
        return INSTANCE;
    }

    private TypeMirrorUtil() {
        //singleton constructor
    }

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
