package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.model.manifest.Receiver;
import org.androidtransfuse.processor.ManifestManager;
import org.androidtransfuse.util.TypeMirrorUtil;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;

/**
 * @author John Ericksen
 */
public class BroadcastReceiverAnalysis {

    private ASTClassFactory astClassFactory;
    private Provider<Receiver> receiverProvider;
    private ManifestManager manifestManager;
    private ComponentBuilderFactory componentBuilderFactory;
    private InjectionNodeBuilderRepository injectionNodeBuilderRepository;
    private IntentFilterBuilder intentFilterBuilder;

    @Inject
    public BroadcastReceiverAnalysis(ASTClassFactory astClassFactory,
                                     Provider<Receiver> receiverProvider,
                                     ManifestManager manifestManager,
                                     ComponentBuilderFactory componentBuilderFactory,
                                     InjectionNodeBuilderRepository injectionNodeBuilderRepository,
                                     IntentFilterBuilder intentFilterBuilder) {
        this.astClassFactory = astClassFactory;
        this.receiverProvider = receiverProvider;
        this.manifestManager = manifestManager;
        this.componentBuilderFactory = componentBuilderFactory;
        this.injectionNodeBuilderRepository = injectionNodeBuilderRepository;
        this.intentFilterBuilder = intentFilterBuilder;
    }

    public ComponentDescriptor analyzeElement(ASTType astType) {

        BroadcastReceiver broadcastReceiver = astType.getAnnotation(BroadcastReceiver.class);

        PackageClass receiverClassName;

        ComponentDescriptor receiverDescriptor = null;

        if (astType.extendsFrom(astClassFactory.buildASTClassType(android.content.BroadcastReceiver.class))) {
            //vanilla Android broadcast receiver
            PackageClass activityPackageClass = new PackageClass(astType.getName());
            receiverClassName = buildPackageClass(astType, activityPackageClass.getClassName());
        } else {
            receiverClassName = buildPackageClass(astType, broadcastReceiver.name());

            TypeMirror type = TypeMirrorUtil.getInstance().getTypeMirror(new ReceiverTypeRunnable(broadcastReceiver));
            String receiverType = buildReceiverType(type);

            receiverDescriptor = new ComponentDescriptor(receiverType, receiverClassName);

            receiverDescriptor.getComponentBuilders().add(componentBuilderFactory.buildOnReceieve(injectionNodeBuilderRepository, astType));
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
        manifestReceiver.setLabel(StringUtils.isBlank(annotation.label()) ? null : annotation.label());
        manifestReceiver.setProcess(StringUtils.isBlank(annotation.process()) ? null : annotation.process());
        manifestReceiver.setPermission(StringUtils.isBlank(annotation.permission()) ? null : annotation.permission());
        manifestReceiver.setIcon(StringUtils.isBlank(annotation.icon()) ? null : annotation.icon());
        manifestReceiver.setEnabled(annotation.enabled() ? null : false);
        manifestReceiver.setExported(annotation.exported() ? null : false);

        manifestReceiver.setIntentFilters(intentFilterBuilder.buildIntentFilters(astType));

        manifestManager.addBroadcastReceiver(manifestReceiver);
    }



    private PackageClass buildPackageClass(ASTType astType, String className) {
        PackageClass inputPackageClass = new PackageClass(astType.getName());

        if (StringUtils.isBlank(className)) {
            return inputPackageClass.add("BroadcastReceiver");
        } else {
            return inputPackageClass.replaceName(className);
        }
    }

    private static final class ReceiverTypeRunnable implements Runnable {

        private BroadcastReceiver receiverAnnotation;

        private ReceiverTypeRunnable(BroadcastReceiver receiverAnnotation) {
            this.receiverAnnotation = receiverAnnotation;
        }

        public void run() {
            //accessing this throws an exception, caught in TypeMiirrorUtil
            receiverAnnotation.type();
        }
    }
}
