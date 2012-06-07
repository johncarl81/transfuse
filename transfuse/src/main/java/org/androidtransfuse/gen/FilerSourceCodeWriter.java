package org.androidtransfuse.gen;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;

import javax.annotation.processing.Filer;
import javax.inject.Inject;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author John Ericksen
 */
public class FilerSourceCodeWriter extends CodeWriter {

    private Filer filer;
    private Collection<OutputStream> openStreams = new HashSet<OutputStream>();

    @Inject
    public FilerSourceCodeWriter(@Assisted Filer filer) {
        this.filer = filer;
    }

    @Override
    public OutputStream openBinary(JPackage jPackage, String fileName) throws IOException {
        JavaFileObject sourceFile = filer.createSourceFile(toQualifiedClassName(jPackage, fileName));

        OutputStream os = sourceFile.openOutputStream();
        openStreams.add(os);

        return os;
    }

    private String toQualifiedClassName(JPackage pkg, String fileName) {
        int suffixPosition = fileName.lastIndexOf('.');
        return pkg.name() + "." + fileName.substring(0, suffixPosition);
    }

    @Override
    public void close() throws IOException {
        for (OutputStream openStream : openStreams) {
            openStream.flush();
            openStream.close();
        }
    }
}
