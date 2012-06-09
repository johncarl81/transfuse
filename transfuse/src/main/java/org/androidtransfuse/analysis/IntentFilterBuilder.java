package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Intent;
import org.androidtransfuse.annotations.IntentFilters;
import org.androidtransfuse.model.manifest.Action;
import org.androidtransfuse.model.manifest.Category;
import org.androidtransfuse.model.manifest.IntentFilter;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class IntentFilterBuilder {

    private Provider<IntentFilter> intentFilterProvider;
    private Provider<Action> actionProvider;
    private Provider<Category> categoryProvider;

    @Inject
    public IntentFilterBuilder(Provider<IntentFilter> intentFilterProvider, Provider<Action> actionProvider, Provider<Category> categoryProvider) {
        this.intentFilterProvider = intentFilterProvider;
        this.actionProvider = actionProvider;
        this.categoryProvider = categoryProvider;
    }

    public List<IntentFilter> buildIntentFilters(ASTType astType) {

        IntentFilters intentFilters = astType.getAnnotation(IntentFilters.class);
        Intent intent = astType.getAnnotation(Intent.class);

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
}
