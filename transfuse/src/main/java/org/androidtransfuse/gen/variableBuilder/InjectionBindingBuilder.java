package org.androidtransfuse.gen.variableBuilder;

import android.content.Context;
import android.preference.PreferenceManager;
import com.google.common.collect.ImmutableList;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class InjectionBindingBuilder {

    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private final ASTClassFactory astClassFactory;

    @Inject
    public InjectionBindingBuilder(VariableInjectionBuilderFactory variableInjectionBuilderFactory, ASTClassFactory astClassFactory) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.astClassFactory = astClassFactory;
    }

    public DependencyBindingBuilder dependency(Class clazz) {
        return new DependencyBindingBuilder(clazz);
    }

    public StaticInvocationBindingBuilder staticInvoke(Class<PreferenceManager> invocationTarget, Class returnType, String method) {
        return new StaticInvocationBindingBuilder(invocationTarget, returnType, method);
    }

    public InjectionNodeBuilder buildThis(Class targetClass) {
        ASTType astType = astClassFactory.getType(targetClass);
        return buildThis(astType);
    }

    public InjectionNodeBuilder buildThis(ASTType targetType) {
        return variableInjectionBuilderFactory.buildInjectionNodeBuilder(
                variableInjectionBuilderFactory.buildIndependentVariableBuilderWrapper(targetType, JExpr._this()));
    }

    public InjectionNodeBuilder buildExpression(TypedExpression typedExpression) {
        return variableInjectionBuilderFactory.buildInjectionNodeBuilder(
                variableInjectionBuilderFactory.buildExpressionWrapper(typedExpression));
    }

    public final class StaticInvocationBindingBuilder {

        private final Class invocationTarget;
        private final Class returnType;
        private final String method;

        private StaticInvocationBindingBuilder(Class invocationTarget, Class returnType, String method) {
            this.invocationTarget = invocationTarget;
            this.returnType = returnType;
            this.method = method;
        }

        public StaticInvocationBindingBuilderArgument dependencyArg(Class<Context> dependency) {
            return new StaticInvocationBindingBuilderArgument(this, dependency);
        }
    }

    public final class StaticInvocationBindingBuilderArgument {

        private final StaticInvocationBindingBuilder parent;
        private final Class dependency;

        private StaticInvocationBindingBuilderArgument(StaticInvocationBindingBuilder parent, Class dependency) {
            this.parent = parent;
            this.dependency = dependency;
        }

        public InjectionNodeBuilder build() {
            return variableInjectionBuilderFactory.buildDependentInjectionNodeBuilder(
                    dependency,
                    parent.returnType,
                    variableInjectionBuilderFactory.buildStaticInvocationVariableBuilder(parent.invocationTarget, parent.method));
        }
    }

    public final class DependencyBindingBuilder {

        private final Class clazz;

        private DependencyBindingBuilder(Class clazz) {
            this.clazz = clazz;
        }

        public DependantVariableBuilderWrapper invoke(Class returnType, DependentVariableBuilder dependentVariableBuilder) {
            return new DependantVariableBuilderWrapper(returnType, dependentVariableBuilder);
        }

        public DependencyArgumentBindingBuilder invoke(Class returnType, String methodName) {
            return new DependencyArgumentBindingBuilder(clazz, returnType, methodName);
        }

        public final class DependencyArgumentBindingBuilder {

            private final Class clazz;
            private final Class returnType;
            private final String methodName;
            private final ImmutableList.Builder<JExpression> arguments = ImmutableList.builder();

            private DependencyArgumentBindingBuilder(Class clazz, Class returnType, String methodName) {
                this.clazz = clazz;
                this.returnType = returnType;
                this.methodName = methodName;
            }

            public DependencyArgumentBindingBuilder arg(JExpression value) {
                arguments.add(value);
                return this;
            }

            public InjectionNodeBuilder build() {
                return variableInjectionBuilderFactory.buildDependentInjectionNodeBuilder(
                        clazz,
                        returnType,
                        variableInjectionBuilderFactory.buildMethodCallVariableBuilder(methodName, arguments.build()));
            }
        }

        public final class DependantVariableBuilderWrapper {
            private final DependentVariableBuilder dependentVariableBuilder;
            private final Class returnType;

            private DependantVariableBuilderWrapper(Class returnType, DependentVariableBuilder dependentVariableBuilder) {
                this.returnType = returnType;
                this.dependentVariableBuilder = dependentVariableBuilder;
            }

            public InjectionNodeBuilder build() {
                return variableInjectionBuilderFactory.buildDependentInjectionNodeBuilder(
                        clazz,
                        returnType,
                        dependentVariableBuilder
                );
            }
        }
    }


}
