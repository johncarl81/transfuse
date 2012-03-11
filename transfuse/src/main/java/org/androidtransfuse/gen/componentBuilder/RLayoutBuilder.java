package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
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

    @Inject
    public RLayoutBuilder(@Assisted Integer layout, RResourceReferenceBuilder rResourceReferenceBuilder) {
        this.layout = layout;
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
    }

    @Override
    public void buildLayoutCall(JBlock block, RResource rResource) {
        //layout setting
        ResourceIdentifier layoutIdentifier = rResource.getResourceIdentifier(layout);
        if (layoutIdentifier != null) {
            block.invoke("setContentView").arg(rResourceReferenceBuilder.buildReference(layoutIdentifier));
        }
    }
}
