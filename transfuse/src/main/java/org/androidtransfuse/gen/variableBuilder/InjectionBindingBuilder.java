/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.gen.variableBuilder;

import com.google.common.collect.ImmutableList;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
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
        return dependency(astClassFactory.getType(clazz));
    }

    public DependencyBindingBuilder dependency(ASTType type) {
        return new DependencyBindingBuilder(type);
    }

    public StaticInvocationBindingBuilder staticInvoke(ASTType invocationTarget, ASTType returnType, String method) {
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

        private final ASTType invocationTarget;
        private final ASTType returnType;
        private final String method;

        private StaticInvocationBindingBuilder(ASTType invocationTarget, ASTType returnType, String method) {
            this.invocationTarget = invocationTarget;
            this.returnType = returnType;
            this.method = method;
        }

        public StaticInvocationBindingBuilderArgument dependencyArg(ASTType dependency) {
            return new StaticInvocationBindingBuilderArgument(this, dependency);
        }
    }

    public final class StaticInvocationBindingBuilderArgument {

        private final StaticInvocationBindingBuilder parent;
        private final ASTType dependency;

        private StaticInvocationBindingBuilderArgument(StaticInvocationBindingBuilder parent, ASTType dependency) {
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

        private final ASTType type;

        private DependencyBindingBuilder(ASTType type) {
            this.type = type;
        }

        public DependantVariableBuilderWrapper invoke(ASTType returnType, DependentVariableBuilder dependentVariableBuilder) {
            return new DependantVariableBuilderWrapper(returnType, dependentVariableBuilder);
        }

        public DependencyArgumentBindingBuilder invoke(ASTType returnType, String methodName) {
            return new DependencyArgumentBindingBuilder(type, returnType, methodName);
        }

        public final class DependencyArgumentBindingBuilder {

            private final ASTType type;
            private final ASTType returnType;
            private final String methodName;
            private final ImmutableList.Builder<JExpression> arguments = ImmutableList.builder();

            private DependencyArgumentBindingBuilder(ASTType type, ASTType returnType, String methodName) {
                this.type = type;
                this.returnType = returnType;
                this.methodName = methodName;
            }

            public DependencyArgumentBindingBuilder arg(JExpression value) {
                arguments.add(value);
                return this;
            }

            public InjectionNodeBuilder build() {
                return variableInjectionBuilderFactory.buildDependentInjectionNodeBuilder(
                        type,
                        returnType,
                        variableInjectionBuilderFactory.buildMethodCallVariableBuilder(methodName, arguments.build()));
            }
        }

        public final class DependantVariableBuilderWrapper {
            private final DependentVariableBuilder dependentVariableBuilder;
            private final ASTType returnType;

            private DependantVariableBuilderWrapper(ASTType returnType, DependentVariableBuilder dependentVariableBuilder) {
                this.returnType = returnType;
                this.dependentVariableBuilder = dependentVariableBuilder;
            }

            public InjectionNodeBuilder build() {
                return variableInjectionBuilderFactory.buildDependentInjectionNodeBuilder(
                        type,
                        returnType,
                        dependentVariableBuilder
                );
            }
        }
    }
}
