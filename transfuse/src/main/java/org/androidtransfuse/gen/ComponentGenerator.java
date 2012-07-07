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
        if (descriptor == null) {
            return;
        }

        try {
            final JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, descriptor.getPackageClass().getFullyQualifiedName(), ClassType.CLASS);

            generatedClassAnnotator.annotateClass(definedClass);

            definedClass._extends(codeModel.ref(descriptor.getType()));

            MethodDescriptor methodDescriptor = descriptor.getMethodBuilder().buildMethod(definedClass);

            JBlock block = methodDescriptor.getMethod().body();

            //Injections
            Map<InjectionNode, TypedExpression> expressionMap =
                    injectionFragmentGenerator.buildFragment(
                        block,
                        definedClass,
                        descriptor.getInjectionNodeFactory().buildInjectionNode(methodDescriptor));

            //Method Callbacks
            MethodGenerator onCreateMethodGenerator = new ExistingMethod(methodDescriptor);
            MethodCallbackGenerator onCreateCallbackGenerator = componentBuilderFactory.buildMethodCallbackGenerator(
                    methodDescriptor.getASTMethod().getName(), onCreateMethodGenerator);

            onCreateCallbackGenerator.generate(definedClass, methodDescriptor, expressionMap, descriptor);

            //... and other listeners
            for (ExpressionVariableDependentGenerator generator : descriptor.getGenerators()) {
                generator.generate(definedClass, methodDescriptor, expressionMap, descriptor);
            }

            descriptor.getMethodBuilder().closeMethod(methodDescriptor);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Class Already Exists ", e);
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("ClassNotFoundException while building Injection Fragment", e);
        }
    }
}
