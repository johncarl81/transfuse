package org.androidtransfuse.gen.variableBuilder;

import android.content.Context;
import android.preference.PreferenceManager;
import com.sun.codemodel.JExpr;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class InjectionBindingBuilder {

    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private ASTClassFactory astClassFactory;

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
        ASTType astType = astClassFactory.buildASTClassType(targetClass);
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

        private Class invocationTarget;
        private Class returnType;
        private String method;

        private StaticInvocationBindingBuilder(Class invocationTarget, Class returnType, String method) {
            this.invocationTarget = invocationTarget;
            this.returnType = returnType;
            this.method = method;
        }

        public StaticInvocationBindingBuilderArgument depenencyArg(Class<Context> dependency) {
            return new StaticInvocationBindingBuilderArgument(this, dependency);
        }
    }

    public final class StaticInvocationBindingBuilderArgument {

        private StaticInvocationBindingBuilder parent;
        private Class dependency;

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

        private Class clazz;

        private DependencyBindingBuilder(Class clazz) {
            this.clazz = clazz;
        }

        public DepenentVariableBuilderWrapper invoke(Class returnType, DependentVariableBuilder dependentVariableBuilder) {
            return new DepenentVariableBuilderWrapper(returnType, dependentVariableBuilder);
        }

        public DependencyArgumentBindingBuilder invoke(Class returnType, String methodName) {
            return new DependencyArgumentBindingBuilder(clazz, returnType, methodName);
        }

        public final class DependencyArgumentBindingBuilder {

            private Class clazz;
            private Class returnType;
            private String methodName;
            private List<String> arguments = new ArrayList<String>();

            private DependencyArgumentBindingBuilder(Class clazz, Class returnType, String methodName) {
                this.clazz = clazz;
                this.returnType = returnType;
                this.methodName = methodName;
            }

            public DependencyArgumentBindingBuilder arg(String value) {
                arguments.add(value);
                return this;
            }

            public InjectionNodeBuilder build() {
                return variableInjectionBuilderFactory.buildDependentInjectionNodeBuilder(
                        clazz,
                        returnType,
                        variableInjectionBuilderFactory.buildMethodCallVariableBuilder(methodName, arguments));
            }
        }

        public final class DepenentVariableBuilderWrapper {
            private DependentVariableBuilder dependentVariableBuilder;
            private Class returnType;

            private DepenentVariableBuilderWrapper(Class returnType, DependentVariableBuilder dependentVariableBuilder) {
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
