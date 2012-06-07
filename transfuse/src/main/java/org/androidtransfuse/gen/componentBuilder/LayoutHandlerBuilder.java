package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class LayoutHandlerBuilder implements LayoutBuilder {

    private InjectionFragmentGenerator injectionFragmentGenerator;
    private InjectionNode layoutHandlerInjectionNode;
    private Logger logger;

    @Inject
    public LayoutHandlerBuilder(InjectionFragmentGenerator injectionFragmentGenerator,
                                @Assisted InjectionNode layoutHandlerInjectionNode,
                                Logger logger) {
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.layoutHandlerInjectionNode = layoutHandlerInjectionNode;
        this.logger = logger;
    }

    @Override
    public void buildLayoutCall(JDefinedClass definedClass, JBlock block, RResource rResource) {

        try {
            Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, layoutHandlerInjectionNode, rResource);

            //LayoutHandlerDelegate.getlayout()
            JExpression layoutHandlerDelegate = expressionMap.get(layoutHandlerInjectionNode).getExpression();

            block.invoke("setContentView").arg(layoutHandlerDelegate.invoke("getLayout"));


        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundExcetion while trying to generate LayoutHandler", e);
        } catch (JClassAlreadyExistsException e) {
            logger.error("JClassAlreadyExistsException while trying to generate LayoutHandler", e);
        }
    }
}
