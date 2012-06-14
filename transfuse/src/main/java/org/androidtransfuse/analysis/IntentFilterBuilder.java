package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Intent;
import org.androidtransfuse.annotations.IntentFilter;
import org.androidtransfuse.annotations.IntentFilters;
import org.androidtransfuse.model.manifest.Action;
import org.androidtransfuse.model.manifest.Category;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class IntentFilterBuilder {

    private Provider<org.androidtransfuse.model.manifest.IntentFilter> intentFilterProvider;
    private Provider<Action> actionProvider;
    private Provider<Category> categoryProvider;

    @Inject
    public IntentFilterBuilder(Provider<org.androidtransfuse.model.manifest.IntentFilter> intentFilterProvider, Provider<Action> actionProvider, Provider<Category> categoryProvider) {
        this.intentFilterProvider = intentFilterProvider;
        this.actionProvider = actionProvider;
        this.categoryProvider = categoryProvider;
    }

    public List<org.androidtransfuse.model.manifest.IntentFilter> buildIntentFilters(ASTType astType) {

        IntentFilters intentFilters = astType.getAnnotation(IntentFilters.class);
        IntentFilter intentFilter = astType.getAnnotation(IntentFilter.class);
        Intent intent = astType.getAnnotation(Intent.class);

        List<org.androidtransfuse.model.manifest.IntentFilter> convertedIntentFilters = new ArrayList<org.androidtransfuse.model.manifest.IntentFilter>();

        if(intentFilters != null){
            for (IntentFilter filter : intentFilters.value()) {
                convertedIntentFilters.add(convertIntentFilter(filter));
            }
        }

        org.androidtransfuse.model.manifest.IntentFilter resultIntentFilter = null;
        if (intentFilter != null) {
            resultIntentFilter = convertIntentFilter(intentFilter);
            convertedIntentFilters.add(resultIntentFilter);
        }
        if (intent != null) {
            if (resultIntentFilter == null) {
                resultIntentFilter = intentFilterProvider.get();
                convertedIntentFilters.add(resultIntentFilter);
            }

            addIntent(intent, resultIntentFilter);
        }

        return convertedIntentFilters;
    }

    private org.androidtransfuse.model.manifest.IntentFilter convertIntentFilter(IntentFilter intentFilter){
        org.androidtransfuse.model.manifest.IntentFilter resultIntentFilter = intentFilterProvider.get();

        for (Intent intentAnnotation : intentFilter.value()) {
            addIntent(intentAnnotation, resultIntentFilter);
        }

        return resultIntentFilter;
    }

    private void addIntent(Intent intentAnnotation, org.androidtransfuse.model.manifest.IntentFilter intentFilter) {
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
}
