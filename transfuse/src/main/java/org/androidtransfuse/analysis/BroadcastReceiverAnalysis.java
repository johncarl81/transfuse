package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.ContextScopeComponentBuilder;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.model.manifest.Receiver;
import org.androidtransfuse.processor.ManifestManager;
import org.androidtransfuse.util.TypeMirrorRunnable;
import org.androidtransfuse.util.TypeMirrorUtil;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;

/**
 * @author John Ericksen
 */
public class BroadcastReceiverAnalysis implements Analysis<ComponentDescriptor> {

    private ASTClassFactory astClassFactory;
    private Provider<Receiver> receiverProvider;
    private ManifestManager manifestManager;
    private ComponentBuilderFactory componentBuilderFactory;
    private IntentFilterBuilder intentFilterBuilder;
    private TypeMirrorUtil typeMirrorUtil;
    private MetaDataBuilder metaDataBuilder;
    private ContextScopeComponentBuilder contextScopeComponentBuilder;

    @Inject
    public BroadcastReceiverAnalysis(ASTClassFactory astClassFactory,
                                     Provider<Receiver> receiverProvider,
                                     ManifestManager manifestManager,
                                     ComponentBuilderFactory componentBuilderFactory,
                                     IntentFilterBuilder intentFilterBuilder,
                                     TypeMirrorUtil typeMirrorUtil,
                                     MetaDataBuilder metaDataBuilder,
                                     ContextScopeComponentBuilder contextScopeComponentBuilder) {
        this.astClassFactory = astClassFactory;
        this.receiverProvider = receiverProvider;
        this.manifestManager = manifestManager;
        this.componentBuilderFactory = componentBuilderFactory;
        this.intentFilterBuilder = intentFilterBuilder;
        this.typeMirrorUtil = typeMirrorUtil;
        this.metaDataBuilder = metaDataBuilder;
        this.contextScopeComponentBuilder = contextScopeComponentBuilder;
    }

    public ComponentDescriptor analyze(ASTType astType) {

        BroadcastReceiver broadcastReceiver = astType.getAnnotation(BroadcastReceiver.class);

        PackageClass receiverClassName;

        ComponentDescriptor receiverDescriptor = null;

        if (astType.extendsFrom(astClassFactory.buildASTClassType(android.content.BroadcastReceiver.class))) {
            //vanilla Android broadcast receiver
            PackageClass activityPackageClass = new PackageClass(astType.getName());
            receiverClassName = buildPackageClass(astType, activityPackageClass.getClassName());
        } else {
            receiverClassName = buildPackageClass(astType, broadcastReceiver.name());

            TypeMirror type = typeMirrorUtil.getTypeMirror(new ReceiverTypeRunnable(broadcastReceiver));
            String receiverType = buildReceiverType(type);

            receiverDescriptor = new ComponentDescriptor(receiverType, receiverClassName);

            receiverDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildBroadcastReceiverInjectionNodeFactory(astType));

            receiverDescriptor.setMethodBuilder(componentBuilderFactory.buildOnReceiveMethodBuilder());

            receiverDescriptor.getComponentBuilders().add(contextScopeComponentBuilder);
        }

        setupManifest(receiverClassName.getFullyQualifiedName(), broadcastReceiver, astType);

        return receiverDescriptor;
    }

    private String buildReceiverType(TypeMirror type) {
        if (type != null) {
            return type.toString();
        } else {
            return android.content.BroadcastReceiver.class.getName();
        }
    }

    private void setupManifest(String name, BroadcastReceiver annotation, ASTType astType) {

        Receiver manifestReceiver = receiverProvider.get();

        manifestReceiver.setName(name);
        manifestReceiver.setLabel(checkBlank(annotation.label()));
        manifestReceiver.setProcess(checkBlank(annotation.process()));
        manifestReceiver.setPermission(checkBlank(annotation.permission()));
        manifestReceiver.setIcon(checkBlank(annotation.icon()));
        manifestReceiver.setEnabled(checkDefault(annotation.enabled(), true));
        manifestReceiver.setExported(checkDefault(annotation.exported(), true));

        manifestReceiver.setIntentFilters(intentFilterBuilder.buildIntentFilters(astType));
        manifestReceiver.setMetaData(metaDataBuilder.buildMetaData(astType));

        manifestManager.addBroadcastReceiver(manifestReceiver);
    }

    private <T> T checkDefault(T input, T defaultValue) {
        if (input.equals(defaultValue)) {
            return null;
        }
        return input;
    }

    private String checkBlank(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        return input;
    }

    private PackageClass buildPackageClass(ASTType astType, String className) {
        PackageClass inputPackageClass = new PackageClass(astType.getName());

        if (StringUtils.isBlank(className)) {
            return inputPackageClass.add("BroadcastReceiver");
        } else {
            return inputPackageClass.replaceName(className);
        }
    }

    private static final class ReceiverTypeRunnable extends TypeMirrorRunnable<BroadcastReceiver> {
        private ReceiverTypeRunnable(BroadcastReceiver receiverAnnotation) {
            super(receiverAnnotation);
        }

        @Override
        public void run(BroadcastReceiver annotation) {
            //accessing this throws an exception, caught in TypeMiirrorUtil
            annotation.type();
        }
    }
}
