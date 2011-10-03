package org.androidrobotics.util;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author John Ericksen
 */
public class FilerSourceCodeWriter extends CodeWriter {

    private Filer filer;

    public FilerSourceCodeWriter(Filer filer) {
        this.filer = filer;
    }

    @Override
    public OutputStream openBinary(JPackage jPackage, String fileName) throws IOException {
        JavaFileObject sourceFile = filer.createSourceFile(toQualifiedClassName(jPackage, fileName));
        return sourceFile.openOutputStream();
    }

    private String toQualifiedClassName(JPackage pkg, String fileName) {
        int suffixPosition = fileName.lastIndexOf('.');
        return pkg.name() + "." + fileName.substring(0, suffixPosition);
    }

    @Override
    public void close() throws IOException {
        System.out.println("CLOSE CALLED");
    }
}
