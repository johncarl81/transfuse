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
package org.androidtransfuse.config;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;

/**
 * @author John Ericksen
 */
public class SynchronizedFiler implements Filer {

    private Filer delegate;

    public SynchronizedFiler(Filer delegate) {
        this.delegate = delegate;
    }

    @Override
    public synchronized JavaFileObject createSourceFile(CharSequence charSequence, Element... elements) throws IOException {
        return delegate.createSourceFile(charSequence, elements);
    }

    @Override
    public synchronized JavaFileObject createClassFile(CharSequence charSequence, Element... elements) throws IOException {
        return delegate.createClassFile(charSequence, elements);
    }

    @Override
    public synchronized FileObject createResource(JavaFileManager.Location location, CharSequence charSequence, CharSequence charSequence1, Element... elements) throws IOException {
        return delegate.createResource(location, charSequence, charSequence1, elements);
    }

    @Override
    public synchronized FileObject getResource(JavaFileManager.Location location, CharSequence charSequence, CharSequence charSequence1) throws IOException {
        return delegate.getResource(location, charSequence, charSequence1);
    }
}
