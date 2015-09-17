/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.intentFactory;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public abstract class ComponentFactory {

    public static final String ADD_FLAG_METHOD = "addFlag";
    public static final String INTERNAL_ADD_FLAG_METHOD = "internalAddFlags";
    public static final String INTERNAL_GET_FLAGS_METHOD = "internalGetFlags";
    public static final String ADD_CATEGORY_METHOD = "addCategory";
    public static final String INTERNAL_ADD_CATEGORY_METHOD = "internalAddCategory";

    private int flags = 0;
    private List<String> categories = new ArrayList<String>();

    protected void internalAddCategory(String category){
        this.categories.add(category);
    }

    protected List<String> getCategories() {
        return categories;
    }

    protected void internalAddFlags(int flag) {
        this.flags |= flag;
    }

    protected int internalGetFlags(){
        return flags;
    }

    public abstract Intent build(Context context);

    public <T extends Context> T start(T context) {
        context.startActivity(build(context));
        return context;
    }
    
    public Activity startActivityForResult(Activity activity) {
        activity.startActivityForResult(build(activity), flags);
        return activity;
    }

    public PendingIntent buildPendingIntent(Context context){
        return buildPendingIntent(0, context);
    }

    public PendingIntent buildPendingIntent(int requestCode, Context context){
        return PendingIntent.getActivity(context, requestCode, build(context), flags);
    }
}
