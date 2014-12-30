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
package org.androidtransfuse.plugins;

import org.androidtransfuse.ConfigurationRepository;
import org.androidtransfuse.TransfusePlugin;
import org.androidtransfuse.adapter.ASTPrimitiveType;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.listeners.FragmentMenuComponent;
import org.androidtransfuse.util.AndroidLiterals;

/**
 * @author John Ericksen
 */
@Bootstrap
public class FragmentPlugin implements TransfusePlugin{
    @Override
    public void run(ConfigurationRepository repository) {

        repository.component(Fragment.class).method("onCreateView", AndroidLiterals.LAYOUT_INFLATER, AndroidLiterals.VIEW_GROUP, AndroidLiterals.BUNDLE).event(OnCreate.class).registration();
        repository.component(Fragment.class).method("onActivityCreated", AndroidLiterals.BUNDLE).event(OnActivityCreated.class).superCall();
        repository.component(Fragment.class).method("onStart").event(OnStart.class).superCall();
        repository.component(Fragment.class).method("onResume").event(OnResume.class).superCall();
        repository.component(Fragment.class).method("onPause").event(OnPause.class).superCall();
        repository.component(Fragment.class).method("onStop").event(OnStop.class).superCall();
        repository.component(Fragment.class).method("onDestroyView").event(OnDestroyView.class).superCall();
        repository.component(Fragment.class).method("onDestroy").event(OnDestroy.class).superCall();
        repository.component(Fragment.class).method("onDetach").event(OnDetach.class).superCall();
        repository.component(Fragment.class).method("onLowMemory").event(OnLowMemory.class).superCall();
        repository.component(Fragment.class).method("onActivityResult", ASTPrimitiveType.INT, ASTPrimitiveType.INT, AndroidLiterals.INTENT).event(OnActivityResult.class);
        repository.component(Fragment.class).method("onConfigurationChanged", AndroidLiterals.CONTENT_CONFIGURATION).event(OnConfigurationChanged.class).superCall();

        repository.component(Fragment.class)
                .extending(AndroidLiterals.LIST_FRAGMENT)
                .method("onListItemClick", AndroidLiterals.LIST_VIEW, AndroidLiterals.VIEW, ASTPrimitiveType.INT, ASTPrimitiveType.LONG).event(OnListItemClick.class);

        repository.component(Fragment.class).callThroughEvent(FragmentMenuComponent.class);

        repository.component(Fragment.class).listener(AndroidLiterals.VIEW_ON_CLICK_LISTENER, AndroidLiterals.VIEW, "setOnClickListener");
        repository.component(Fragment.class).listener(AndroidLiterals.VIEW_ON_LONG_CLICK_LISTENER, AndroidLiterals.VIEW, "setOnLongClickListener");
        repository.component(Fragment.class).listener(AndroidLiterals.VIEW_ON_CREATE_CONTEXT_MENU_LISTENER, AndroidLiterals.VIEW, "setOnCreateContextMenuListener");
        repository.component(Fragment.class).listener(AndroidLiterals.VIEW_ON_KEY_LISTENER, AndroidLiterals.VIEW, "setOnKeyListener");
        repository.component(Fragment.class).listener(AndroidLiterals.VIEW_ON_TOUCH_LISTENER, AndroidLiterals.VIEW, "setOnTouchListener");
        repository.component(Fragment.class).listener(AndroidLiterals.VIEW_ON_FOCUS_CHANGE_LISTENER, AndroidLiterals.VIEW, "setOnFocusChangeListener");
        repository.component(Fragment.class).listener(AndroidLiterals.ADAPTER_VIEW_ON_ITEM_CLICK_LISTENER, AndroidLiterals.ADAPTER_VIEW, "setOnItemClickListener");
        repository.component(Fragment.class).listener(AndroidLiterals.ADAPTER_VIEW_ON_ITEM_LONG_CLICK_LISTENER, AndroidLiterals.ADAPTER_VIEW, "setOnItemLongClickListener");
        repository.component(Fragment.class).listener(AndroidLiterals.ADAPTER_VIEW_ON_ITEM_SELECTED_LISTENER, AndroidLiterals.ADAPTER_VIEW, "setOnItemSelectedListener");
        repository.component(Fragment.class).listener(AndroidLiterals.ABS_LIST_VIEW_ON_SCROLL_LISTENER, AndroidLiterals.ABS_LIST_VIEw, "setOnScrollListener");
        repository.component(Fragment.class).listener(AndroidLiterals.ABS_LIST_VIEW_MULTI_CHOICE_MODE_LISTENER, AndroidLiterals.ABS_LIST_VIEw, "setMultiChoiceModeListener");
        repository.component(Fragment.class).listener(AndroidLiterals.ABS_LIST_VIEW_RECYCLER_LISTENER, AndroidLiterals.ABS_LIST_VIEw, "setViewRecyclerListener");
        repository.component(Fragment.class).listener(AndroidLiterals.TEXT_VIEW_ON_EDITOR_ACTION_LISTENER, AndroidLiterals.TEXT_VIEW, "setOnEditorActionListener");

    }
}
