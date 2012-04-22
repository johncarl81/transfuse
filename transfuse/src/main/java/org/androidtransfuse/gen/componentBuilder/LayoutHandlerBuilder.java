package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResource;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class LayoutHandlerBuilder implements LayoutBuilder {

    private InjectionFragmentGenerator injectionFragmentGenerator;
    private InjectionNode layoutHandlerInjectionNode;

    @Inject
    public LayoutHandlerBuilder(InjectionFragmentGenerator injectionFragmentGenerator,
                                @Assisted InjectionNode layoutHandlerInjectionNode) {
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.layoutHandlerInjectionNode = layoutHandlerInjectionNode;
    }

    @Override
    public void buildLayoutCall(JDefinedClass definedClass, JBlock block, RResource rResource) {

        try {
            Map<InjectionNode, JExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, layoutHandlerInjectionNode, rResource);

            //LayoutHandlerDelegate.getlayout()
            JExpression layoutHandlerDelegate = expressionMap.get(layoutHandlerInjectionNode);

            block.invoke("setContentView").arg(layoutHandlerDelegate.invoke("getLayout"));


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JClassAlreadyExistsException e) {
            e.printStackTrace();
        }
    }
}
