package org.androidtransfuse.gen;

import javax.annotation.processing.Filer;

/**
 * @author John Ericksen
 */
public interface CodeWriterFactory {
    FilerSourceCodeWriter buildSourceWriter(Filer filer);

    ResourceCodeWriter buildResourceWriter(Filer filer);

}
