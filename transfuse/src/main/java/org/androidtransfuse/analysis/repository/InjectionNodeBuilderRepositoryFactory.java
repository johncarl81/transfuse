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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.sun.codemodel.JExpr;
import org.androidtransfuse.adapter.ASTStringType;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.module.ModuleRepository;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author John Ericksen
 */
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

    private static final ImmutableMap<String, ASTType> SYSTEM_SERVICES;

    static{
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

        SYSTEM_SERVICES = systemServiceBuilder.build();
    }

    @Singleton
    public static final class InjectionNodeRepository{

        private final Set<ASTType> installedComponents = new HashSet<ASTType>();
        private final InjectionNodeBuilderRepository moduleRepository;

        @Inject
        public InjectionNodeRepository(InjectionNodeBuilderRepository moduleRepository){
            this.moduleRepository = moduleRepository;
        }
    }

    private final InjectionNodeRepository repository;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider;
    private final ScopeAspectFactoryRepositoryProvider scopeAspectFactoryRepositoryProvider;

    @Inject
    public InjectionNodeBuilderRepositoryFactory(InjectionBindingBuilder injectionBindingBuilder,
                                                 Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider,
                                                 ScopeAspectFactoryRepositoryProvider scopeAspectFactoryRepositoryProvider,
                                                 InjectionNodeRepository repository) {
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.injectionNodeBuilderRepositoryProvider = injectionNodeBuilderRepositoryProvider;
        this.scopeAspectFactoryRepositoryProvider = scopeAspectFactoryRepositoryProvider;
        this.repository = repository;
    }

    public InjectionNodeBuilderRepository buildApplicationInjections() {
        InjectionNodeBuilderRepository builderRepository = injectionNodeBuilderRepositoryProvider.get();
        //resources
        builderRepository.putType(AndroidLiterals.RESOURCES, injectionBindingBuilder.dependency(AndroidLiterals.APPLICATION).invoke(AndroidLiterals.RESOURCES, "getResources").build());

        //menu inflater
        builderRepository.putType(AndroidLiterals.MENU_INFLATER, injectionBindingBuilder.dependency(AndroidLiterals.ACTIVITY).invoke(AndroidLiterals.MENU_INFLATER, "getMenuInflater").build());

        //system services
        for (Map.Entry<String, ASTType> systemServiceEntry : SYSTEM_SERVICES.entrySet()) {
            builderRepository.putType(systemServiceEntry.getValue(),
                    injectionBindingBuilder.dependency(AndroidLiterals.CONTEXT).invoke(new ASTStringType("java.lang.Object"), "getSystemService").arg(JExpr.lit(systemServiceEntry.getKey())).build());
        }

        builderRepository.putType(AndroidLiterals.SHARED_PREFERENCES,
                injectionBindingBuilder.staticInvoke(AndroidLiterals.PREFERENCE_MANAGER, AndroidLiterals.SHARED_PREFERENCES, "getDefaultSharedPreferences").dependencyArg(AndroidLiterals.CONTEXT).build());


        return builderRepository;
    }

    public InjectionNodeBuilderRepository buildModuleConfiguration() {
        InjectionNodeBuilderRepository builderRepository = injectionNodeBuilderRepositoryProvider.get();
        builderRepository.addRepository(this.repository.moduleRepository);
        builderRepository.addRepository(scopeAspectFactoryRepositoryProvider.get());
        builderRepository.addRepository(injectionNodeBuilderRepositoryProvider.get());

        return builderRepository;
    }

    @Override
    public Collection<ASTType> getInstalledAnnotatedWith(Class<? extends Annotation> annotation) {
        ImmutableSet.Builder<ASTType> installedBuilder = ImmutableSet.builder();

        for (ASTType installedComponent : repository.installedComponents) {
            if(installedComponent.isAnnotated(annotation)){
                installedBuilder.add(installedComponent);
            }
        }

        return installedBuilder.build();
    }

    @Override
    public void addInstalledComponents(ASTType[] astType) {
        repository.installedComponents.addAll(Arrays.asList(astType));
    }

    @Override
    public void addModuleRepository(InjectionNodeBuilderRepository repository) {
        this.repository.moduleRepository.addRepository(repository);
    }
}
