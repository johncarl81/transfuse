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
package org.androidtransfuse.util;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * @author John Ericksen
 */
@Bootstrap
public class AndroidLiteralsTest {

    @Inject
    private ASTClassFactory astClassFactory;

    @Before
    public void setup(){
        Bootstraps.inject(this);
    }

    @Test
    public void testLiterals(){
        assertEquals(AndroidLiterals.ACTIVITY, astClassFactory.getType(Activity.class));
        assertEquals(AndroidLiterals.LIST_ACTIVITY, astClassFactory.getType(ListActivity.class));
        assertEquals(AndroidLiterals.APPLICATION, astClassFactory.getType(Application.class));
        assertEquals(AndroidLiterals.CONTEXT, astClassFactory.getType(Context.class));
        assertEquals(AndroidLiterals.BUNDLE, astClassFactory.getType(Bundle.class));
        assertEquals(AndroidLiterals.CONTENT_CONFIGURATION, astClassFactory.getType(Configuration.class));
        assertEquals(AndroidLiterals.BROADCAST_RECEIVER, astClassFactory.getType(BroadcastReceiver.class));
        assertEquals(AndroidLiterals.INTENT, astClassFactory.getType(Intent.class));
        assertEquals(AndroidLiterals.SERVICE, astClassFactory.getType(Service.class));
        assertEquals(AndroidLiterals.IBINDER, astClassFactory.getType(IBinder.class));
        assertEquals(AndroidLiterals.FRAGMENT, astClassFactory.getType(Fragment.class));
        assertEquals(AndroidLiterals.FRAGMENT_MANAGER, astClassFactory.getType(FragmentManager.class));
        assertEquals(AndroidLiterals.LIST_FRAGMENT, astClassFactory.getType(ListFragment.class));
        assertEquals(AndroidLiterals.LAYOUT_INFLATER, astClassFactory.getType(LayoutInflater.class));
        assertEquals(AndroidLiterals.VIEW, astClassFactory.getType(View.class));
        assertEquals(AndroidLiterals.VIEW_GROUP, astClassFactory.getType(ViewGroup.class));
        assertEquals(AndroidLiterals.LIST_VIEW, astClassFactory.getType(ListView.class));
        assertEquals(AndroidLiterals.VIEW_ON_CLICK_LISTENER, astClassFactory.getType(View.OnClickListener.class));
        assertEquals(AndroidLiterals.VIEW_ON_LONG_CLICK_LISTENER, astClassFactory.getType(View.OnLongClickListener.class));
        assertEquals(AndroidLiterals.VIEW_ON_CREATE_CONTEXT_MENU_LISTENER, astClassFactory.getType(View.OnCreateContextMenuListener.class));
        assertEquals(AndroidLiterals.VIEW_ON_KEY_LISTENER, astClassFactory.getType(View.OnKeyListener.class));
        assertEquals(AndroidLiterals.VIEW_ON_TOUCH_LISTENER, astClassFactory.getType(View.OnTouchListener.class));
        assertEquals(AndroidLiterals.VIEW_ON_FOCUS_CHANGE_LISTENER, astClassFactory.getType(View.OnFocusChangeListener.class));
        assertEquals(AndroidLiterals.PREFERENCE_ACTIVITY, astClassFactory.getType(PreferenceActivity.class));
        assertEquals(AndroidLiterals.ACTIVITY_GROUP, astClassFactory.getType(ActivityGroup.class));
        assertEquals(AndroidLiterals.RESOURCES, astClassFactory.getType(Resources.class));
        assertEquals(AndroidLiterals.PREFERENCE_MANAGER, astClassFactory.getType(PreferenceManager.class));
        assertEquals(AndroidLiterals.SHARED_PREFERENCES, astClassFactory.getType(SharedPreferences.class));
        assertEquals(AndroidLiterals.MENU_INFLATER, astClassFactory.getType(MenuInflater.class));
        assertEquals(AndroidLiterals.PARCELABLE, astClassFactory.getType(Parcelable.class));
        assertEquals(AndroidLiterals.COLOR_STATE_LIST, astClassFactory.getType(ColorStateList.class));
        assertEquals(AndroidLiterals.GRAPHICS_MOVIE, astClassFactory.getType(Movie.class));
        assertEquals(AndroidLiterals.GRAPHICS_DRAWABLE, astClassFactory.getType(Drawable.class));
        assertEquals(AndroidLiterals.ANIMATION, astClassFactory.getType(Animation.class));
        assertEquals(AndroidLiterals.ANIMATION_UTILS, astClassFactory.getType(AnimationUtils.class));
    }
}
