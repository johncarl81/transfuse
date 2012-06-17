package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.gen.componentBuilder.*;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ComponentGenerator implements Generator<ComponentDescriptor> {

    private JCodeModel codeModel;
    private GeneratedClassAnnotator generatedClassAnnotator;
    private InjectionFragmentGenerator injectionFragmentGenerator;
    private ComponentBuilderFactory componentBuilderFactory;

    @Inject
    public ComponentGenerator(JCodeModel codeModel,
                              GeneratedClassAnnotator generatedClassAnnotator,
                              InjectionFragmentGenerator injectionFragmentGenerator,
                              ComponentBuilderFactory componentBuilderFactory) {
        this.codeModel = codeModel;
        this.generatedClassAnnotator = generatedClassAnnotator;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.componentBuilderFactory = componentBuilderFactory;
    }

    public void generate(ComponentDescriptor descriptor) {

        try {
            final JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, descriptor.getPackageClass().getFullyQualifiedName(), ClassType.CLASS);

            generatedClassAnnotator.annotateClass(definedClass);

            definedClass._extends(codeModel.ref(descriptor.getType()));

            MethodDescriptor onCreateMethodDescriptor = descriptor.getMethodBuilder().buildMethod(definedClass);

            JBlock block = onCreateMethodDescriptor.getMethod().body();

            //Injections
            Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, descriptor.getInjectionNodeFactory().buildInjectionNode(onCreateMethodDescriptor));

            //Method Callbacks
            MethodGenerator onCreateMethodGenerator = new ExistingMethod(onCreateMethodDescriptor);
            MethodCallbackGenerator onCreateCallbackGenerator = componentBuilderFactory.buildMethodCallbackGenerator(
                    onCreateMethodDescriptor.getASTMethod().getName(), onCreateMethodGenerator);

            onCreateCallbackGenerator.generate(definedClass, block, expressionMap, descriptor);

            //... and other listener
            for (ExpressionVariableDependentGenerator generator : descriptor.getGenerators()) {
                generator.generate(definedClass, block, expressionMap, descriptor);
            }

            for (ComponentBuilder componentBuilder : descriptor.getComponentBuilders()) {
                componentBuilder.build(definedClass, descriptor);
            }
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Class Already Exists ", e);
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("ClassNotFoundException while building Injection Fragment", e);
        }
    }
}
