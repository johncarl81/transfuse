package org.androidrobotics.util;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Locale;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class EmptyProcessingEnvironment implements ProcessingEnvironment {
    @Override
    public Map<String, String> getOptions() {
        return null;
    }

    @Override
    public Messager getMessager() {
        return null;
    }

    @Override
    public Filer getFiler() {
        return null;
    }

    @Override
    public Elements getElementUtils() {
        return null;
    }

    @Override
    public Types getTypeUtils() {
        return null;
    }

    @Override
    public SourceVersion getSourceVersion() {
        return null;
    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
