package org.androidtransfuse.gen;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.TransfuseAnnotationProcessor;
import org.androidtransfuse.util.Generated;

import java.text.DateFormat;
import java.util.Date;

/**
 * Generation class centralizing the addition of the Generated annotation:
 * {@code @Generated(value = "org.androidtransfuse.TransfuseAnnotationProcessor", date = "7/12/12 10:08 AM")}
 *
 * @author John Ericksen
 */
public class GeneratedClassAnnotator {

    /**
     * Annotates the input class with the {@code @Generated} annotation
     *
     * @param definedClass input codemodel class
     */
    public void annotateClass(JDefinedClass definedClass) {
        definedClass.annotate(Generated.class)
                .param("value", TransfuseAnnotationProcessor.class.getName())
                .param("date", DateFormat.getInstance().format(new Date()));
    }
}
