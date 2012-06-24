package org.androidtransfuse.gen.componentBuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
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
    private ASTClassFactory astClassFactory;

    @Inject
    public FragmentSimpleLayoutBuilder(@Assisted Integer layout,
                                       UniqueVariableNamer namer,
                                       RResourceReferenceBuilder rResourceReferenceBuilder,
                                       ASTClassFactory astClassFactory) {
        this.layout = layout;
        this.namer = namer;
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
        this.astClassFactory = astClassFactory;
    }

    @Override
    public void buildLayoutCall(JDefinedClass definedClass, JBlock block) {
        //onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        JMethod onCreateView = definedClass.method(JMod.PUBLIC, View.class, "onCreateView");

        JVar inflater = onCreateView.param(LayoutInflater.class, namer.generateName(LayoutInflater.class));
        JVar container = onCreateView.param(ViewGroup.class, namer.generateName(ViewGroup.class));
        JVar savedInstanceState = onCreateView.param(Bundle.class, namer.generateName(Bundle.class));

        //inflater.inflate(R.layout.details, container, false);
        onCreateView.body()._return(inflater.invoke("inflate")
                .arg(rResourceReferenceBuilder.buildReference(layout))
                .arg(container)
                .arg(JExpr.lit(false)));
    }

    private ASTMethod buildOnCreateViewMethod() {
        try{
            return astClassFactory.buildASTClassMethod(Fragment.class.getMethod("onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class));
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("Unable to find onCreateView method on Fragment", e);
        }

    }
}
