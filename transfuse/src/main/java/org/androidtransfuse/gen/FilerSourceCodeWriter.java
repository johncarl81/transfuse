/**
 * Copyright 2012 John Ericksen
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
import org.androidtransfuse.model.PackageClass;

import javax.annotation.processing.Filer;
import javax.inject.Inject;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;

/**
 * Adapter class to allow codemodel to write its output source and source files to the Java Annotation Processor Filer
 *
 * @author John Ericksen
 */
public class FilerSourceCodeWriter extends CodeWriter {

    private final Filer filer;
    private final Collection<OutputStream> openStreams = new HashSet<OutputStream>();

    @Inject
    public FilerSourceCodeWriter(Filer filer) {
        this.filer = filer;
    }

    @Override
    public OutputStream openBinary(JPackage jPackage, String fileName) throws IOException {
        //generate a source file based on package and filename
        JavaFileObject sourceFile = filer.createSourceFile(toQualifiedClassName(jPackage, fileName));

        OutputStream os = sourceFile.openOutputStream();
        openStreams.add(os);

        return os;
    }

    private String toQualifiedClassName(JPackage pkg, String fileName) {
        return new PackageClass(pkg.name(), fileName).getFullyQualifiedName();
    }

    @Override
    public void close() throws IOException {
        for (OutputStream openStream : openStreams) {
            openStream.flush();
            openStream.close();
        }
    }
}
