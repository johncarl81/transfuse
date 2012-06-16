package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class OnCreateComponentBuilder implements ComponentBuilder {

    private InjectionNodeFactory injectionNodeFactory;
    private InjectionFragmentGenerator injectionFragmentGenerator;
    private Set<ExpressionVariableDependentGenerator> generators = new HashSet<ExpressionVariableDependentGenerator>();
    private LayoutBuilder layoutBuilder;
    private ComponentBuilderFactory componentBuilderFactory;
    private MethodBuilder methodBuilder;

    @Inject
    public OnCreateComponentBuilder(@Assisted InjectionNodeFactory injectionNodeFactory,
                                    @Assisted LayoutBuilder layoutBuilder,
                                    @Assisted MethodBuilder methodBuilder,
                                    InjectionFragmentGenerator injectionFragmentGenerator,
                                    ComponentBuilderFactory componentBuilderFactory) {
        this.injectionNodeFactory = injectionNodeFactory;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.componentBuilderFactory = componentBuilderFactory;
        this.layoutBuilder = layoutBuilder;
        this.methodBuilder = methodBuilder;
    }

    @Override
    public void build(JDefinedClass definedClass, ComponentDescriptor descriptor) {
        try {
            MethodDescriptor onCreateMethodDescriptor = methodBuilder.buildMethod(definedClass);

            JBlock block = onCreateMethodDescriptor.getMethod().body();

            //Layout todo:move? only makes sense in Activities
            layoutBuilder.buildLayoutCall(definedClass, block);

            //Injections
            Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, injectionNodeFactory.buildInjectionNode(onCreateMethodDescriptor));

            //Method Callbacks
            MethodGenerator onCreateMethodGenerator = new ExistingMethod(onCreateMethodDescriptor);
            MethodCallbackGenerator onCreateCallbackGenerator = componentBuilderFactory.buildMethodCallbackGenerator(
                    onCreateMethodDescriptor.getASTMethod().getName(), onCreateMethodGenerator);

            onCreateCallbackGenerator.generate(definedClass, block, expressionMap, descriptor);

            //... and other listener
            for (ExpressionVariableDependentGenerator generator : generators) {
                generator.generate(definedClass, block, expressionMap, descriptor);
            }
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("ClassNotFoundException while building Injection Fragment", e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("JClassAlreadyExistsException while building Injection Fragment", e);
        }

    }

    public void addMethodCallbackBuilder(ExpressionVariableDependentGenerator methodCallbackGenerator) {
        this.generators.add(methodCallbackGenerator);
    }

    public void addMethodCallbackBuilders(Set<ExpressionVariableDependentGenerator> generators) {
        this.generators.addAll(generators);
    }
}
