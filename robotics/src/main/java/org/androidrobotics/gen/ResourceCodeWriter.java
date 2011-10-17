package org.androidrobotics.gen;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;

public class ResourceCodeWriter extends CodeWriter {

    private final Filer filer;
    private Collection<OutputStream> openStreams = new HashSet<OutputStream>();

    public ResourceCodeWriter(Filer filer) {
        this.filer = filer;
    }

    @Override
    public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
        FileObject resource = filer.createResource(StandardLocation.SOURCE_OUTPUT, pkg.name(), fileName);

        OutputStream os = resource.openOutputStream();
        openStreams.add(os);

        return os;
    }


    @Override
    public void close() throws IOException {
        for (OutputStream openStream : openStreams) {
            openStream.flush();
            openStream.close();
        }
    }
}