/**
 * Copyright 2013 John Ericksen
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
package org.androidtransfuse.plugins;

import org.androidtransfuse.ConfigurationRepository;
import org.androidtransfuse.TransfusePlugin;
import org.androidtransfuse.adapter.ASTPrimitiveType;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.listeners.*;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Bootstrap
public class ActivityPlugin implements TransfusePlugin{

    @Inject
    InjectionBindingBuilder injectionBindingBuilder;

    @Override
    public void run(ConfigurationRepository repository) {

        repository.component(Activity.class).method("onCreate", AndroidLiterals.BUNDLE)
                .event(OnCreate.class)
                .superCall()
                .registration();
        repository.component(Activity.class).method("onDestroy").event(OnDestroy.class).superCall();
        repository.component(Activity.class).method("onPause").event(OnPause.class).superCall();
        repository.component(Activity.class).method("onRestart").event(OnRestart.class).superCall();
        repository.component(Activity.class).method("onResume").event(OnResume.class).superCall();
        repository.component(Activity.class).method("onStart").event(OnStart.class).superCall();
        repository.component(Activity.class).method("onStop").event(OnStop.class).superCall();
        repository.component(Activity.class).method("onBackPressed").event(OnBackPressed.class).superCall();
        repository.component(Activity.class).method("onPostCreate", AndroidLiterals.BUNDLE).event(OnPostCreate.class).superCall();
        repository.component(Activity.class).method("onActivityResult", ASTPrimitiveType.INT, ASTPrimitiveType.INT, AndroidLiterals.INTENT).event(OnActivityResult.class);
        repository.component(Activity.class).method("onNewIntent", AndroidLiterals.INTENT).event(OnNewIntent.class);
        repository.component(Activity.class).method("onSaveInstanceState", AndroidLiterals.BUNDLE).event(OnSaveInstanceState.class).superCall();
        repository.component(Activity.class).method("onRestoreInstanceState", AndroidLiterals.BUNDLE).event(OnRestoreInstanceState.class).superCall();
        repository.component(Activity.class).extending(AndroidLiterals.LIST_ACTIVITY)
                .method("onListItemClick", AndroidLiterals.LIST_VIEW, AndroidLiterals.VIEW, ASTPrimitiveType.INT, ASTPrimitiveType.LONG).event(OnListItemClick.class);

        repository.component(Activity.class).callThroughEvent(ActivityMenuComponent.class);
        repository.component(Activity.class).callThroughEvent(ActivityOnKeyDownListener.class);
        repository.component(Activity.class).callThroughEvent(ActivityOnKeyLongPressListener.class);
        repository.component(Activity.class).callThroughEvent(ActivityOnKeyMultipleListener.class);
        repository.component(Activity.class).callThroughEvent(ActivityOnKeyUpListener.class);
        repository.component(Activity.class).callThroughEvent(ActivityOnTouchEventListener.class);
        repository.component(Activity.class).callThroughEvent(ActivityOnTrackballEventListener.class);

        repository.component(Activity.class).listener(AndroidLiterals.VIEW_ON_CLICK_LISTENER, AndroidLiterals.VIEW, "setOnClickListener");
        repository.component(Activity.class).listener(AndroidLiterals.VIEW_ON_LONG_CLICK_LISTENER, AndroidLiterals.VIEW, "setOnLongClickListener");
        repository.component(Activity.class).listener(AndroidLiterals.VIEW_ON_CREATE_CONTEXT_MENU_LISTENER, AndroidLiterals.VIEW, "setOnCreateContextMenuListener");
        repository.component(Activity.class).listener(AndroidLiterals.VIEW_ON_KEY_LISTENER, AndroidLiterals.VIEW, "setOnKeyListener");
        repository.component(Activity.class).listener(AndroidLiterals.VIEW_ON_TOUCH_LISTENER, AndroidLiterals.VIEW, "setOnTouchListener");
        repository.component(Activity.class).listener(AndroidLiterals.VIEW_ON_FOCUS_CHANGE_LISTENER, AndroidLiterals.VIEW, "setOnFocusChangeListener");
        repository.component(Activity.class).listener(AndroidLiterals.ADAPTER_VIEW_ON_ITEM_CLICK_LISTENER, AndroidLiterals.ADAPTER_VIEW, "setOnItemClickListener");
        repository.component(Activity.class).listener(AndroidLiterals.ADAPTER_VIEW_ON_ITEM_LONG_CLICK_LISTENER, AndroidLiterals.ADAPTER_VIEW, "setOnItemLongClickListener");
        repository.component(Activity.class).listener(AndroidLiterals.ADAPTER_VIEW_ON_ITEM_SELECTED_LISTENER, AndroidLiterals.ADAPTER_VIEW, "setOnItemSelectedListener");
        repository.component(Activity.class).listener(AndroidLiterals.ABS_LIST_VIEW_ON_SCROLL_LISTENER, AndroidLiterals.ABS_LIST_VIEw, "setOnScrollListener");
        repository.component(Activity.class).listener(AndroidLiterals.ABS_LIST_VIEW_MULTI_CHOICE_MODE_LISTENER, AndroidLiterals.ABS_LIST_VIEw, "setMultiChoiceModeListener");
        repository.component(Activity.class).listener(AndroidLiterals.ABS_LIST_VIEW_RECYCLER_LISTENER, AndroidLiterals.ABS_LIST_VIEw, "setViewRecyclerListener");
        repository.component(Activity.class).listener(AndroidLiterals.TEXT_VIEW_ON_EDITOR_ACTION_LISTENER, AndroidLiterals.TEXT_VIEW, "setOnEditorActionListener");

    }
}
