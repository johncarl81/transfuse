package org.androidtransfuse.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.gen.componentBuilder.*;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ComponentGenerator implements Generator<ComponentDescriptor> {

    private final JCodeModel codeModel;
    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final ClassGenerationUtil generationUtil;

    @Inject
    public ComponentGenerator(JCodeModel codeModel,
                              InjectionFragmentGenerator injectionFragmentGenerator,
                              ComponentBuilderFactory componentBuilderFactory,
                              ClassGenerationUtil generationUtil) {
        this.codeModel = codeModel;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.componentBuilderFactory = componentBuilderFactory;
        this.generationUtil = generationUtil;
    }

    public void generate(ComponentDescriptor descriptor) {
        if (descriptor == null) {
            return;
        }

        try {
            final JDefinedClass definedClass = generationUtil.defineClass(descriptor.getPackageClass());

            definedClass._extends(codeModel.ref(descriptor.getType()));

            MethodDescriptor initMethodDescriptor = descriptor.getInitMethodBuilder().buildMethod(definedClass);

            JBlock block = initMethodDescriptor.getMethod().body();

            //Injections
            Map<InjectionNode, TypedExpression> expressionMap =
                    injectionFragmentGenerator.buildFragment(
                            block,
                            definedClass,
                            descriptor.getInjectionNodeFactory().buildInjectionNode(initMethodDescriptor));

            //Registrations
            for (ExpressionVariableDependentGenerator registrationGenerator : descriptor.getRegistrations()) {
                registrationGenerator.generate(definedClass, initMethodDescriptor, expressionMap, descriptor);
            }

            //Method Callbacks
            MethodGenerator onCreateMethodGenerator = new ExistingMethod(initMethodDescriptor);
            MethodCallbackGenerator onCreateCallbackGenerator = componentBuilderFactory.buildMethodCallbackGenerator(
                    descriptor.getInitMethodEventAnnotation(), onCreateMethodGenerator);

            onCreateCallbackGenerator.generate(definedClass, initMethodDescriptor, expressionMap, descriptor);

            //... and other listeners
            for (ExpressionVariableDependentGenerator generator : descriptor.getGenerators()) {
                generator.generate(definedClass, initMethodDescriptor, expressionMap, descriptor);
            }

            descriptor.getInitMethodBuilder().closeMethod(initMethodDescriptor);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Class Already Exists ", e);
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("ClassNotFoundException while building Injection Fragment", e);
        }
    }
}
