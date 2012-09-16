package org.androidtransfuse;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;

/**
 * Empty filer for testing placeholder purposes.
 *
 * @author John Ericksen
 */
public class NoOpFiler implements Filer {
    @Override
    public JavaFileObject createSourceFile(CharSequence charSequence, Element... elements) throws IOException {
        return null;
    }

    @Override
    public JavaFileObject createClassFile(CharSequence charSequence, Element... elements) throws IOException {
        return null;
    }

    @Override
    public FileObject createResource(JavaFileManager.Location location, CharSequence charSequence, CharSequence charSequence1, Element... elements) throws IOException {
        return null;
    }

    @Override
    public FileObject getResource(JavaFileManager.Location location, CharSequence charSequence, CharSequence charSequence1) throws IOException {
        return null;
    }
}
