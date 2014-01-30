package org.androidtransfuse.bootstrap;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.gen.ClassGenerationStrategy;
import org.androidtransfuse.util.Generated;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author John Ericksen
 */
public class BootstrapClassGenerationStrategy implements ClassGenerationStrategy {

    // ISO 8601 standard date format
    private static final DateFormat ISO_8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

    @Override
    public void annotateGenerated(JDefinedClass definedClass) {
        definedClass.annotate(Generated.class)
                .param("value", BootstrapProcessor.class.getName())
                .param("date", ISO_8601.format(new Date()));
    }
}
