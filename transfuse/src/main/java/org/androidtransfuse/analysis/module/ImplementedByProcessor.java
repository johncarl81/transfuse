package org.androidtransfuse.analysis.module;

import com.google.inject.ImplementedBy;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.adapter.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.util.TypeMirrorRunnable;

import javax.inject.Inject;
import javax.lang.model.type.TypeMirror;

import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * @author John Ericksen
 */
public class ImplementedByProcessor {

    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilders;
    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;

    @Inject
    public ImplementedByProcessor(InjectionNodeBuilderRepositoryFactory injectionNodeBuilders, VariableInjectionBuilderFactory variableInjectionBuilderFactory, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
    }


    public void processType(ASTType astType) {
        ImplementedBy annotation = astType.getAnnotation(ImplementedBy.class);

        if (annotation != null) {
            TypeMirror implementedClass = getTypeMirror(new ImplementedByClassTypeMirrorRunnable(annotation));

            ASTType implAstType = implementedClass.accept(astTypeBuilderVisitor, null);

            if (!implAstType.inheritsFrom(astType)) {
                throw new TransfuseAnalysisException("ImplementedBy configuration points to a class that doesn't inherit from the given base class");
            }

            injectionNodeBuilders.putModuleConfig(astType,
                    variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(implAstType));
        }
    }

    private static class ImplementedByClassTypeMirrorRunnable extends TypeMirrorRunnable<ImplementedBy> {

        protected ImplementedByClassTypeMirrorRunnable(ImplementedBy annotation) {
            super(annotation);
        }

        @Override
        public void run(ImplementedBy annotation) {
            annotation.value();
        }
    }
}
