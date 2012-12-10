package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * Module processing class for handling Method annotations.  Pulls the return type and annotation value field
 * and supplies them to the abstract innerProcess method.
 *
 * @author John Ericksen
 */
public abstract class MethodProcessor {

    public ModuleConfiguration process(ASTMethod astMethod, ASTAnnotation astAnnotation) {
        ASTType returnType = astMethod.getReturnType();
        ASTType astType = astAnnotation.getProperty("value", ASTType.class);
        return new PostProcessingModuleConfiguration(returnType, astType);
    }

    public abstract void innerProcess(ASTType returnType, ASTType annotationValue);

    private final class PostProcessingModuleConfiguration implements ModuleConfiguration {

        private final ASTType returnType;
        private final ASTType astType;

        private PostProcessingModuleConfiguration(ASTType returnType, ASTType astType) {
            this.returnType = returnType;
            this.astType = astType;
        }

        @Override
        public void setConfiguration() {
            innerProcess(returnType, astType);
        }
    }
}