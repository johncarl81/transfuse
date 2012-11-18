package org.androidtransfuse.gen;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JPackage;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;

import static org.androidtransfuse.gen.GeneratedClassAnnotator.annotateGeneratedClass;

/**
 * Utility class unifying the creation of a basic class from a PackageClass
 *
 * @author John Ericksen
 */
public class ClassGenerationUtil {

    private JCodeModel codeModel;
    private Logger log;

    @Inject
    public ClassGenerationUtil(JCodeModel codeModel, Logger log) {
        this.codeModel = codeModel;
        this.log = log;
    }

    public JDefinedClass defineClass(PackageClass className) throws JClassAlreadyExistsException {
        JPackage jPackage = codeModel._package(className.getPackage());
        JDefinedClass definedClass = jPackage._class(className.getClassName());

        annotateGeneratedClass(definedClass);

        log.info("Generated " + className);

        return definedClass;
    }
}
