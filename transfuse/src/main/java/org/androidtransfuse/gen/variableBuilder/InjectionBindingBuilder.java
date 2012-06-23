package org.androidtransfuse.gen.variableBuilder;

import android.content.Context;
import android.preference.PreferenceManager;

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

    public StaticInvocationBindingBuilder staticInvoke(Class<PreferenceManager> invocationTarget, String method) {
        return new StaticInvocationBindingBuilder(invocationTarget, method);
    }

    public class StaticInvocationBindingBuilder{

        private Class invocationTarget;
        private String method;

        public StaticInvocationBindingBuilder(Class invocationTarget, String method) {
            this.invocationTarget = invocationTarget;
            this.method = method;
        }


        public StaticInvocationBindingBuilderArgument depenencyArg(Class<Context> dependency) {
            return new StaticInvocationBindingBuilderArgument(invocationTarget, method, dependency);
        }

        /*public InjectionNodeBuilder build(){
           maybe?
        }*/
    }

    public class StaticInvocationBindingBuilderArgument{

        private Class invocationTarget;
        private String method;
        private Class dependency;

        public StaticInvocationBindingBuilderArgument(Class invocationTarget, String method, Class dependency) {
            this.invocationTarget = invocationTarget;
            this.method = method;
            this.dependency = dependency;
        }

        public InjectionNodeBuilder build(){
            return variableInjectionBuilderFactory.buildDependentInjectionNodeBuilder(
                    dependency,
                    variableInjectionBuilderFactory.buildStaticInvocationVariableBuilder(invocationTarget, method));
        }
    }

    public class DependencyBindingBuilder{

        private Class clazz;

        public DependencyBindingBuilder(Class clazz) {
            this.clazz = clazz;
        }

        public DepenentVariableBuilderWrapper invoke(DependentVariableBuilder dependentVariableBuilder){
            return new DepenentVariableBuilderWrapper(dependentVariableBuilder);
        }

        public DependencyArgumentBindingBuilder invoke(String methodName) {
            return new DependencyArgumentBindingBuilder(clazz, methodName);
        }

        public class DependencyArgumentBindingBuilder{

            private Class clazz;
            private String methodName;
            private List<String> arguments = new ArrayList<String>();

            public DependencyArgumentBindingBuilder(Class clazz, String methodName) {
                this.clazz = clazz;
                this.methodName = methodName;
            }

            public DependencyArgumentBindingBuilder arg(String value){
                arguments.add(value);
                return this;
            }

            public InjectionNodeBuilder build(){
                return variableInjectionBuilderFactory.buildDependentInjectionNodeBuilder(
                        clazz,
                        variableInjectionBuilderFactory.buildMethodCallVariableBuilder(methodName, arguments));
            }
        }

        public class DepenentVariableBuilderWrapper{
            private DependentVariableBuilder dependentVariableBuilder;

            public DepenentVariableBuilderWrapper(DependentVariableBuilder dependentVariableBuilder) {
                this.dependentVariableBuilder = dependentVariableBuilder;
            }

            public InjectionNodeBuilder build(){
                return variableInjectionBuilderFactory.buildDependentInjectionNodeBuilder(
                        clazz,
                        dependentVariableBuilder
                );
            }
        }
    }


}
