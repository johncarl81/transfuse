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

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author John Ericksen
 */
public class MessagerLogger implements Logger {

    private final String prepend;
    private final Messager messager;

    public MessagerLogger(Messager messager) {
        this("", messager);
    }

    public MessagerLogger(String prepend, Messager messager) {
        this.messager = messager;
        this.prepend = prepend;
    }

    @Override
    public void info(String value) {
        messager.printMessage(Diagnostic.Kind.NOTE, prepend + value);
    }

    @Override
    public void warning(String value) {
        messager.printMessage(Diagnostic.Kind.WARNING, prepend + value);
    }

    @Override
    public void error(String value) {
        messager.printMessage(Diagnostic.Kind.ERROR, prepend + value);
    }

    @Override
    public void error(String s, Throwable e) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(outputStream));
        messager.printMessage(Diagnostic.Kind.ERROR, prepend + s + "\n" + outputStream.toString());
    }

    @Override
    public void debug(String value) {
        info(value);
    }
}
