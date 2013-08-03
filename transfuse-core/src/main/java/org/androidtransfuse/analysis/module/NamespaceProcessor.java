package org.androidtransfuse.analysis.module;

import com.sun.codemodel.JExpr;
import org.androidtransfuse.adapter.classes.ASTClassFactory;

import com.google.common.collect.ImmutableMap;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTDefinedAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.annotations.ScopeReference;
import org.androidtransfuse.gen.variableBuilder.VariableFactoryBuilderFactory2;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.util.matcher.Matchers;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author John Ericksen
 */
public class NamespaceProcessor implements TypeProcessor {

    private final ASTClassFactory astClassFactory;
    private final VariableFactoryBuilderFactory2 variableFactoryBuilderFactory;
    private final TypedExpressionFactory typedExpressionFactory;

    @Inject
    public NamespaceProcessor(ASTClassFactory astClassFactory,
                              VariableFactoryBuilderFactory2 variableFactoryBuilderFactory,
                              TypedExpressionFactory typedExpressionFactory) {
        this.astClassFactory = astClassFactory;
        this.variableFactoryBuilderFactory = variableFactoryBuilderFactory;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    @Override
    public ModuleConfiguration process(ASTType moduleType, ASTAnnotation typeAnnotation) {
        String namespace = typeAnnotation.getProperty("value", String.class);

        return new NamespaceConfiguration(namespace);
    }

    private final class NamespaceConfiguration implements ModuleConfiguration {

        private final String namespace;

        private NamespaceConfiguration(String namespace) {
            this.namespace = namespace;
        }

        @Override
        public void setConfiguration(InjectionNodeBuilderRepository configurationRepository) {

            ASTType stringType = astClassFactory.getType(String.class);
            ASTType namedType = astClassFactory.getType(Named.class);
            ASTAnnotation namedNamespace = new ASTDefinedAnnotation(namedType, ImmutableMap.of("value", "namespace"));

            configurationRepository.putSignatureMatcher(
                    Matchers.type(stringType).annotated().byAnnotation(namedNamespace).build(),
                    variableFactoryBuilderFactory.buildInjectionNodeBuilder(
                            variableFactoryBuilderFactory.buildExpressionWrapper(
                                    typedExpressionFactory.build(String.class, JExpr.lit(namespace))))
                    );
        }
    }
}
