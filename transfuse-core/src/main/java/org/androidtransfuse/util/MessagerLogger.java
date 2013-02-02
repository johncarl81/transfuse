/**
 * Copyright 2013 John Ericksen
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
