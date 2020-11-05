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
package org.androidtransfuse.gen;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;
import org.androidtransfuse.util.apache.commons.WriterOutputStream;


import javax.annotation.processing.Filer;
import javax.inject.Inject;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;

public class FilerResourceWriter extends CodeWriter {

    private final Filer filer;
    private final Collection<OutputStream> openStreams = new HashSet<OutputStream>();

    @Inject
    public FilerResourceWriter(Filer filer) {
        this.filer = filer;
    }

    @Override
    public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
        FileObject resource = filer.createResource(StandardLocation.SOURCE_OUTPUT, pkg.name(), fileName);
        WriterOutputStream os = new WriterOutputStream(resource.openWriter(),"UTF-8");
        openStreams.add(os);
        return os;
    }


    @Override
    public void close() throws IOException {
        for (OutputStream openStream : openStreams) {
//            openStream.flush();
            openStream.close();
        }
    }
}