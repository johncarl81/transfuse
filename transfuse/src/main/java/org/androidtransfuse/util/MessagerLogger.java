package org.androidtransfuse.util;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * @author John Ericksen
 */
public class MessagerLogger implements Logger {

    private final Messager messager;

    public MessagerLogger(Messager messager) {
        this.messager = messager;
    }

    @Override
    public void info(String value) {
        messager.printMessage(Diagnostic.Kind.NOTE, value);
    }

    @Override
    public void warning(String value) {
        messager.printMessage(Diagnostic.Kind.WARNING, value);
    }

    @Override
    public void error(String value) {
        messager.printMessage(Diagnostic.Kind.ERROR, value);
    }

    @Override
    public void error(String s, Throwable e) {
        messager.printMessage(Diagnostic.Kind.ERROR, s + "\n" + e.getMessage());
    }
}
