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
package org.androidtransfuse.analysis.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.MenuInflater;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.sun.codemodel.JExpr;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTStringType;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.module.ModuleRepository;
import org.androidtransfuse.gen.scopeBuilder.CustomScopeAspectFactoryFactory;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.util.matcher.Matcher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author John Ericksen
 */
@Singleton
public class InjectionNodeBuilderRepositoryFactory implements ModuleRepository {

    public static final java.lang.String POWER_SERVICE = "power";
    public static final java.lang.String WINDOW_SERVICE = "window";
    public static final java.lang.String LAYOUT_INFLATER_SERVICE = "layout_inflater";
    public static final java.lang.String ACCOUNT_SERVICE = "account";
    public static final java.lang.String ACTIVITY_SERVICE = "activity";
    public static final java.lang.String ALARM_SERVICE = "alarm";
    public static final java.lang.String NOTIFICATION_SERVICE = "notification";
    public static final java.lang.String ACCESSIBILITY_SERVICE = "accessibility";
    public static final java.lang.String KEYGUARD_SERVICE = "keyguard";
    public static final java.lang.String LOCATION_SERVICE = "location";
    public static final java.lang.String SEARCH_SERVICE = "search";
    public static final java.lang.String SENSOR_SERVICE = "sensor";
    public static final java.lang.String STORAGE_SERVICE = "storage";
    public static final java.lang.String WALLPAPER_SERVICE = "wallpaper";
    public static final java.lang.String VIBRATOR_SERVICE = "vibrator";
    public static final java.lang.String CONNECTIVITY_SERVICE = "connectivity";
    public static final java.lang.String WIFI_SERVICE = "wifi";
    public static final java.lang.String WIFI_P2P_SERVICE = "wifip2p";
    public static final java.lang.String NSD_SERVICE = "servicediscovery";
    public static final java.lang.String AUDIO_SERVICE = "audio";
    public static final java.lang.String MEDIA_ROUTER_SERVICE = "media_router";
    public static final java.lang.String TELEPHONY_SERVICE = "phone";
    public static final java.lang.String CLIPBOARD_SERVICE = "clipboard";
    public static final java.lang.String INPUT_METHOD_SERVICE = "input_method";
    public static final java.lang.String TEXT_SERVICES_MANAGER_SERVICE = "textservices";
    public static final java.lang.String DROPBOX_SERVICE = "dropbox";
    public static final java.lang.String DEVICE_POLICY_SERVICE = "device_policy";
    public static final java.lang.String UI_MODE_SERVICE = "uimode";
    public static final java.lang.String DOWNLOAD_SERVICE = "download";
    public static final java.lang.String NFC_SERVICE = "nfc";
    public static final java.lang.String USB_SERVICE = "usb";
    public static final java.lang.String INPUT_SERVICE = "input";

    private final Map<InjectionSignature, InjectionNodeBuilder> moduleConfiguration = new HashMap<InjectionSignature, InjectionNodeBuilder>();
    private final Map<Matcher<InjectionSignature>, InjectionNodeBuilder> injectionSignatureConfig = new HashMap<Matcher<InjectionSignature>, InjectionNodeBuilder>();
    private final ImmutableMap<String, ASTType> systemServices;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final Map<ASTType, ASTType> scopeConfiguration = new HashMap<ASTType, ASTType>();
    private final CustomScopeAspectFactoryFactory customScopeAspectFactoryFactory;
    private final Set<ASTType> installedComponents = new HashSet<ASTType>();
    private final Map<ASTType, ASTType> scoping = new HashMap<ASTType, ASTType>();

    @Inject
    public InjectionNodeBuilderRepositoryFactory(InjectionBindingBuilder injectionBindingBuilder,
                                                 CustomScopeAspectFactoryFactory customScopeAspectFactoryFactory) {
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.customScopeAspectFactoryFactory = customScopeAspectFactoryFactory;

        ImmutableMap.Builder<String, ASTType> systemServiceBuilder = ImmutableMap.builder();
        //mapping to class by string to avoid differences in Android platform compilation problems.
        systemServiceBuilder.put(ACCESSIBILITY_SERVICE, new ASTStringType("android.view.accessibility.AccessibilityManager"));
        systemServiceBuilder.put(ACCOUNT_SERVICE, new ASTStringType("android.accounts.AccountManager"));
        systemServiceBuilder.put(ACTIVITY_SERVICE, new ASTStringType("android.app.ActivityManager"));
        systemServiceBuilder.put(ALARM_SERVICE, new ASTStringType("android.app.AlarmManager"));
        systemServiceBuilder.put(AUDIO_SERVICE, new ASTStringType("android.media.AudioManager"));
        systemServiceBuilder.put(CLIPBOARD_SERVICE, new ASTStringType("android.text.ClipboardManager"));
        systemServiceBuilder.put(CONNECTIVITY_SERVICE, new ASTStringType("android.net.ConnectivityManager"));
        systemServiceBuilder.put(DEVICE_POLICY_SERVICE, new ASTStringType("android.app.admin.DevicePolicyManager"));
        systemServiceBuilder.put(DOWNLOAD_SERVICE, new ASTStringType("android.app.DownloadManager"));
        systemServiceBuilder.put(DROPBOX_SERVICE, new ASTStringType("android.os.DropBoxManager"));
        systemServiceBuilder.put(INPUT_METHOD_SERVICE, new ASTStringType("android.view.inputmethod.InputMethodManager"));
        systemServiceBuilder.put(INPUT_SERVICE, new ASTStringType("android.hardware.input.InputManager"));
        systemServiceBuilder.put(KEYGUARD_SERVICE, new ASTStringType("android.app.KeyguardManager"));
        systemServiceBuilder.put(LAYOUT_INFLATER_SERVICE, new ASTStringType("android.view.LayoutInflater"));
        systemServiceBuilder.put(LOCATION_SERVICE, new ASTStringType("android.location.LocationManager"));
        systemServiceBuilder.put(MEDIA_ROUTER_SERVICE, new ASTStringType("android.media.MediaRouter"));
        systemServiceBuilder.put(NFC_SERVICE, new ASTStringType("android.nfc.NfcManager"));
        systemServiceBuilder.put(NSD_SERVICE, new ASTStringType("android.net.nsd.NsdManager"));
        systemServiceBuilder.put(NOTIFICATION_SERVICE, new ASTStringType("android.app.NotificationManager"));
        systemServiceBuilder.put(POWER_SERVICE, new ASTStringType("android.os.PowerManager"));
        systemServiceBuilder.put(SEARCH_SERVICE, new ASTStringType("android.app.SearchManager"));
        systemServiceBuilder.put(SENSOR_SERVICE, new ASTStringType("android.hardware.SensorManager"));
        systemServiceBuilder.put(STORAGE_SERVICE, new ASTStringType("android.os.storage.StorageManager"));
        systemServiceBuilder.put(TELEPHONY_SERVICE, new ASTStringType("android.telephony.TelephonyManager"));
        systemServiceBuilder.put(TEXT_SERVICES_MANAGER_SERVICE, new ASTStringType("android.view.textservice.TextServicesManager"));
        systemServiceBuilder.put(UI_MODE_SERVICE, new ASTStringType("android.app.UiModeManager"));
        systemServiceBuilder.put(USB_SERVICE, new ASTStringType("android.hardware.usb.UsbManager"));
        systemServiceBuilder.put(VIBRATOR_SERVICE, new ASTStringType("android.os.Vibrator"));
        systemServiceBuilder.put(WALLPAPER_SERVICE, new ASTStringType("android.service.wallpaper.WallpaperService"));
        systemServiceBuilder.put(WIFI_P2P_SERVICE, new ASTStringType("android.net.wifi.p2p.WifiP2pManager"));
        systemServiceBuilder.put(WIFI_SERVICE, new ASTStringType("android.net.wifi.WifiManager"));
        systemServiceBuilder.put(WINDOW_SERVICE, new ASTStringType("android.view.WindowManager"));

        systemServices = systemServiceBuilder.build();
    }

    public void addApplicationInjections(InjectionNodeBuilderRepository repository) {
        //resources
        repository.putType(Resources.class, injectionBindingBuilder.dependency(android.app.Application.class).invoke(Resources.class, "getResources").build());

        //menu inflator
        repository.putType(MenuInflater.class, injectionBindingBuilder.dependency(android.app.Activity.class).invoke(MenuInflater.class, "getMenuInflater").build());

        //system services
        for (Map.Entry<String, ASTType> systemServiceEntry : systemServices.entrySet()) {
            repository.putType(systemServiceEntry.getValue(),
                    injectionBindingBuilder.dependency(Context.class).invoke(Object.class, "getSystemService").arg(JExpr.lit(systemServiceEntry.getKey())).build());
        }

        repository.putType(SharedPreferences.class,
                injectionBindingBuilder.staticInvoke(PreferenceManager.class, SharedPreferences.class, "getDefaultSharedPreferences").dependencyArg(Context.class).build());

    }

    public void addModuleConfiguration(InjectionNodeBuilderRepository repository) {
        for (Map.Entry<InjectionSignature, InjectionNodeBuilder> astTypeInjectionNodeBuilderEntry : moduleConfiguration.entrySet()) {
            repository.putType(astTypeInjectionNodeBuilderEntry.getKey(), astTypeInjectionNodeBuilderEntry.getValue());
        }

        for (Map.Entry<Matcher<InjectionSignature>, InjectionNodeBuilder> matcherInjectionNodeBuilderEntry : injectionSignatureConfig.entrySet()) {
            repository.putSignatureMatcher(matcherInjectionNodeBuilderEntry.getKey(), matcherInjectionNodeBuilderEntry.getValue());
        }
    }

    public void putModuleConfig(ASTType type, InjectionNodeBuilder injectionNodeBuilder) {
        InjectionSignature injectionSignature = new InjectionSignature(type, ImmutableSet.<ASTAnnotation>of());
        if(moduleConfiguration.containsKey(injectionSignature)){
            throw new TransfuseAnalysisException("Binding for type already exists: " + type.toString());
        }
        moduleConfiguration.put(injectionSignature, injectionNodeBuilder);
    }

    public void putInjectionSignatureConfig(Matcher<InjectionSignature> type, InjectionNodeBuilder injectionNodeBuilder) {
        if(injectionSignatureConfig.containsKey(type)){
            throw new TransfuseAnalysisException("Binding for type already exists: " + type.toString());
        }
        injectionSignatureConfig.put(type, injectionNodeBuilder);
    }

    @Override
    public void putInjectionSignatureConfig(InjectionSignature injectionSignature, InjectionNodeBuilder injectionNodeBuilder) {
        if(moduleConfiguration.containsKey(injectionSignature)){
            throw new TransfuseAnalysisException("Binding for type already exists: " + injectionSignature.toString());
        }
        moduleConfiguration.put(injectionSignature, injectionNodeBuilder);
    }

    @Override
    public void putScopeConfig(ScopeAspectFactoryRepository scopedVariableBuilderRepository) {
        for (Map.Entry<ASTType, ASTType> astTypeASTTypeEntry : scopeConfiguration.entrySet()) {
            scopedVariableBuilderRepository.putAspectFactory(astTypeASTTypeEntry.getKey(), astTypeASTTypeEntry.getValue(),
                    customScopeAspectFactoryFactory.buildScopeBuilder(astTypeASTTypeEntry.getKey()));
        }
    }

    public void addScopeConfig(ASTType annotation, ASTType scope){
        scopeConfiguration.put(annotation, scope);
    }

    @Override
    public Collection<ASTType> getInstalledAnnotatedWith(Class<? extends Annotation> annotation) {
        ImmutableSet.Builder<ASTType> installedBuilder = ImmutableSet.builder();

        for (ASTType installedComponent : installedComponents) {
            if(installedComponent.isAnnotated(annotation)){
                installedBuilder.add(installedComponent);
            }
        }

        return installedBuilder.build();
    }

    @Override
    public void addInstalledComponents(ASTType[] astType) {
        installedComponents.addAll(Arrays.asList(astType));
    }

    @Override
    public ASTType getScope(ASTType astType) {
        if(scoping.containsKey(astType)){
            return scoping.get(astType);
        }
        return null;
    }

    @Override
    public void putScoped(ASTType scope, ASTType toBeScoped) {
        scoping.put(toBeScoped, scope);
    }
}
