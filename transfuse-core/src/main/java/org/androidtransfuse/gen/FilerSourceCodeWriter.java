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
import org.androidtransfuse.adapter.PackageClass;

import javax.annotation.processing.Filer;
import javax.inject.Inject;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;

/**
 * Adapter class to allow codemodel to write its output source and source files to the Java Annotation Processor Filer
 *
 * @author John Ericksen
 */
public class FilerSourceCodeWriter extends CodeWriter {

    private final Filer filer;
    private final Originating originating;
    private final Collection<OutputStream> openStreams = new HashSet<OutputStream>();

    @Inject
    public FilerSourceCodeWriter(Filer filer, Originating originating) {
        this.filer = filer;
        this.originating = originating;
    }

    @Override
    public OutputStream openBinary(JPackage jPackage, String fileName) throws IOException {
        //generate a source file based on package and fileName
        String qualified = toQualifiedClassName(jPackage, fileName);
        JavaFileObject sourceFile = filer.createSourceFile(qualified, originating.getOriginatingElements(qualified));
        OutputStream os = getWriterOutputStream(sourceFile.openWriter());
        openStreams.add(os);

        return os;
    }

    public OutputStream getWriterOutputStream(Writer writer) {
        return new WriterOutputStream(writer, Charset.forName("UTF-8"));
    }

    private String toQualifiedClassName(JPackage pkg, String fileName) {
        return new PackageClass(pkg.name(), fileName).getFullyQualifiedName();
    }

    @Override
    public void close() throws IOException {
        for (OutputStream openStream : openStreams) {
            openStream.close();
        }
    }
}
