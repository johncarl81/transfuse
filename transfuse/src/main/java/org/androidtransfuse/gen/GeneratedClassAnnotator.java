package org.androidtransfuse.gen;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.TransfuseAnnotationProcessor;
import org.androidtransfuse.util.Generated;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author John Ericksen
 */
public class GeneratedClassAnnotator {

    public void annotateClass(JDefinedClass definedClass) {
        definedClass.annotate(Generated.class)
                .param("value", TransfuseAnnotationProcessor.class.getName())
                .param("date", DateFormat.getInstance().format(new Date()));
    }
}
