package org.androidtransfuse.examples.hello;

import org.androidtransfuse.annotations.*;

@Activity(label = "@string/app_name")
@IntentFilters({
        @Intent(type = IntentType.ACTION, name = "android.intent.action.MAIN"),
        @Intent(type = IntentType.CATEGORY, name = "android.intent.category.LAUNCHER")
})
@Layout(R.layout.main)
public class HelloAndroid {
}
