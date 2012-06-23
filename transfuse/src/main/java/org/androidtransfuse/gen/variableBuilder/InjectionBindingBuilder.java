package org.androidtransfuse.gen.variableBuilder;

import android.content.Context;
import android.preference.PreferenceManager;
import com.sun.codemodel.JExpr;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class InjectionBindingBuilder {

    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public InjectionBindingBuilder(VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    public DependencyBindingBuilder dependency(Class clazz){
        return new DependencyBindingBuilder(clazz);
    }

    public StaticInvocationBindingBuilder staticInvoke(Class<PreferenceManager> invocationTarget, Class returnType, String method) {
        return new StaticInvocationBindingBuilder(invocationTarget, returnType, method);
    }

    public InjectionNodeBuilder buildThis(Class targetClass) {
        return variableInjectionBuilderFactory.buildInjectionNodeBuilder(
                variableInjectionBuilderFactory.buildIndependentVariableBuilderWrapper(targetClass, JExpr._this()));
    }

    public InjectionNodeBuilder buildExpression(TypedExpression typedExpression) {
        return variableInjectionBuilderFactory.buildInjectionNodeBuilder(
                variableInjectionBuilderFactory.buildExpressionWrapper(typedExpression));
    }

    public class StaticInvocationBindingBuilder{

        private Class invocationTarget;
        private Class returnType;
        private String method;

        public StaticInvocationBindingBuilder(Class invocationTarget, Class returnType, String method) {
            this.invocationTarget = invocationTarget;
            this.returnType = returnType;
            this.method = method;
        }


        public StaticInvocationBindingBuilderArgument depenencyArg(Class<Context> dependency) {
            return new StaticInvocationBindingBuilderArgument(invocationTarget, returnType, method, dependency);
        }

        /*public InjectionNodeBuilder build(){
           maybe?
        }*/
    }

    public class StaticInvocationBindingBuilderArgument{

        private Class invocationTarget;
        private Class returnType;
        private String method;
        private Class dependency;

        public StaticInvocationBindingBuilderArgument(Class invocationTarget, Class returnType, String method, Class dependency) {
            this.invocationTarget = invocationTarget;
            this.returnType = returnType;
            this.method = method;
            this.dependency = dependency;
        }

        public InjectionNodeBuilder build(){
            return variableInjectionBuilderFactory.buildDependentInjectionNodeBuilder(
                    dependency,
                    returnType,
                    variableInjectionBuilderFactory.buildStaticInvocationVariableBuilder(invocationTarget, method));
        }
    }

    public class DependencyBindingBuilder{

        private Class clazz;

        public DependencyBindingBuilder(Class clazz) {
            this.clazz = clazz;
        }

        public DepenentVariableBuilderWrapper invoke(Class returnType, DependentVariableBuilder dependentVariableBuilder){
            return new DepenentVariableBuilderWrapper(returnType, dependentVariableBuilder);
        }

        public DependencyArgumentBindingBuilder invoke(Class returnType, String methodName) {
            return new DependencyArgumentBindingBuilder(clazz, returnType, methodName);
        }

        public class DependencyArgumentBindingBuilder{

            private Class clazz;
            private Class returnType;
            private String methodName;
            private List<String> arguments = new ArrayList<String>();

            public DependencyArgumentBindingBuilder(Class clazz, Class returnType, String methodName) {
                this.clazz = clazz;
                this.returnType = returnType;
                this.methodName = methodName;
            }

            public DependencyArgumentBindingBuilder arg(String value){
                arguments.add(value);
                return this;
            }

            public InjectionNodeBuilder build(){
                return variableInjectionBuilderFactory.buildDependentInjectionNodeBuilder(
                        clazz,
                        returnType,
                        variableInjectionBuilderFactory.buildMethodCallVariableBuilder(methodName, arguments));
            }
        }

        public class DepenentVariableBuilderWrapper{
            private DependentVariableBuilder dependentVariableBuilder;
            private Class returnType;

            public DepenentVariableBuilderWrapper(Class returnType, DependentVariableBuilder dependentVariableBuilder) {
                this.returnType = returnType;
                this.dependentVariableBuilder = dependentVariableBuilder;
            }

            public InjectionNodeBuilder build(){
                return variableInjectionBuilderFactory.buildDependentInjectionNodeBuilder(
                        clazz,
                        returnType,
                        dependentVariableBuilder
                );
            }
        }
    }


}
