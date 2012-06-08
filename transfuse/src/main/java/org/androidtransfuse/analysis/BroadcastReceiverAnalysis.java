package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.annotations.Intent;
import org.androidtransfuse.annotations.IntentFilters;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.model.manifest.Action;
import org.androidtransfuse.model.manifest.Category;
import org.androidtransfuse.model.manifest.IntentFilter;
import org.androidtransfuse.model.manifest.Receiver;
import org.androidtransfuse.processor.ManifestManager;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class BroadcastReceiverAnalysis {

    private ASTClassFactory astClassFactory;
    private Provider<Receiver> receiverProvider;
    private Provider<IntentFilter> intentFilterProvider;
    private Provider<Action> actionProvider;
    private Provider<Category> categoryProvider;
    private ManifestManager manifestManager;

    @Inject
    public BroadcastReceiverAnalysis(ASTClassFactory astClassFactory,
                                     Provider<Receiver> receiverProvider,
                                     Provider<IntentFilter> intentFilterProvider,
                                     Provider<Category> categoryProvider,
                                     Provider<Action> actionProvider,
                                     ManifestManager manifestManager) {
        this.astClassFactory = astClassFactory;
        this.receiverProvider = receiverProvider;
        this.intentFilterProvider = intentFilterProvider;
        this.categoryProvider = categoryProvider;
        this.actionProvider = actionProvider;
        this.manifestManager = manifestManager;
    }

    public ComponentDescriptor analyzeElement(ASTType astType) {

        BroadcastReceiver broadcastReceiver = astType.getAnnotation(BroadcastReceiver.class);
        IntentFilters intentFilters = astType.getAnnotation(IntentFilters.class);
        Intent intent = astType.getAnnotation(Intent.class);

        PackageClass receiverClassName;

        if (astType.extendsFrom(astClassFactory.buildASTClassType(android.content.BroadcastReceiver.class))) {
            //vanilla Android broadcast receiver
            PackageClass activityPackageClass = new PackageClass(astType.getName());
            receiverClassName = buildPackageClass(astType, activityPackageClass.getClassName());
        } else {
            receiverClassName = buildPackageClass(astType, broadcastReceiver.name());
            //TODO:Implement

        }

        setupManifest(receiverClassName.getFullyQualifiedName(), broadcastReceiver.label(), intentFilters, intent);

        return null;
    }

    private void setupManifest(String name, String label, IntentFilters intentFilters, Intent intent) {

        Receiver manifestReceiver = receiverProvider.get();

        manifestReceiver.setName(name);
        manifestReceiver.setLabel(StringUtils.isBlank(label) ? null : label);
        manifestReceiver.setIntentFilters(buildIntentFilters(intentFilters, intent));

        manifestManager.addBroadcastReceiver(manifestReceiver);
    }

    private List<IntentFilter> buildIntentFilters(IntentFilters intentFilters, Intent intent) {
        List<IntentFilter> convertedIntentFilters = new ArrayList<IntentFilter>();

        IntentFilter intentFilter = null;
        if (intentFilters != null) {
            intentFilter = intentFilterProvider.get();
            convertedIntentFilters.add(intentFilter);

            for (Intent intentAnnotation : intentFilters.value()) {
                addIntent(intentAnnotation, intentFilter);
            }
        }
        if (intent != null) {
            if (intentFilter == null) {
                intentFilter = intentFilterProvider.get();
                convertedIntentFilters.add(intentFilter);
            }

            addIntent(intent, intentFilter);
        }

        return convertedIntentFilters;
    }

    private void addIntent(Intent intentAnnotation, IntentFilter intentFilter) {
        switch (intentAnnotation.type()) {
            case ACTION:
                Action action = actionProvider.get();
                action.setName(intentAnnotation.name());
                intentFilter.getActions().add(action);
                break;
            case CATEGORY:
                Category category = categoryProvider.get();
                category.setName(intentAnnotation.name());
                intentFilter.getCategories().add(category);
                break;
            default:
                //noop
                break;
        }
    }

    private PackageClass buildPackageClass(ASTType astType, String className) {
        PackageClass inputPackageClass = new PackageClass(astType.getName());

        if (StringUtils.isBlank(className)) {
            return inputPackageClass.add("BroadcastReceiver");
        } else {
            return inputPackageClass.replaceName(className);
        }
    }
}
