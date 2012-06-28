package org.androidtransfuse.gen.componentBuilder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.r.RResourceReferenceBuilder;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class FragmentSimpleLayoutBuilder implements LayoutBuilder {

    private Integer layout;
    private UniqueVariableNamer namer;
    private RResourceReferenceBuilder rResourceReferenceBuilder;

    @Inject
    public FragmentSimpleLayoutBuilder(@Assisted Integer layout,
                                       UniqueVariableNamer namer,
                                       RResourceReferenceBuilder rResourceReferenceBuilder) {
        this.layout = layout;
        this.namer = namer;
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
    }

    @Override
    public void buildLayoutCall(JDefinedClass definedClass, JBlock block) {
        //onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        JMethod onCreateView = definedClass.method(JMod.PUBLIC, View.class, "onCreateView");

        JVar inflater = onCreateView.param(LayoutInflater.class, namer.generateName(LayoutInflater.class));
        JVar container = onCreateView.param(ViewGroup.class, namer.generateName(ViewGroup.class));
        onCreateView.param(Bundle.class, namer.generateName(Bundle.class));

        //inflater.inflate(R.layout.details, container, false);
        onCreateView.body()._return(inflater.invoke("inflate")
                .arg(rResourceReferenceBuilder.buildReference(layout))
                .arg(container)
                .arg(JExpr.lit(false)));
    }
}
