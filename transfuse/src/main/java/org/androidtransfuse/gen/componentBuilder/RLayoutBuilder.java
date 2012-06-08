package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.model.r.RResourceReferenceBuilder;
import org.androidtransfuse.model.r.ResourceIdentifier;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class RLayoutBuilder implements LayoutBuilder {

    private Integer layout;
    private RResourceReferenceBuilder rResourceReferenceBuilder;
    private RResource rResource;

    @Inject
    public RLayoutBuilder(@Assisted Integer layout, RResourceReferenceBuilder rResourceReferenceBuilder, RResource rResource) {
        this.layout = layout;
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
        this.rResource = rResource;
    }

    @Override
    public void buildLayoutCall(JDefinedClass definedClass, JBlock block) {
        //layout setting
        ResourceIdentifier layoutIdentifier = rResource.getResourceIdentifier(layout);
        if (layoutIdentifier != null) {
            block.invoke("setContentView").arg(rResourceReferenceBuilder.buildReference(layoutIdentifier));
        }
    }
}
