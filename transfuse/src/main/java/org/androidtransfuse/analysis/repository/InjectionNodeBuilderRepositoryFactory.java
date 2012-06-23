package org.androidtransfuse.analysis.repository;

import android.accounts.AccountManager;
import android.app.*;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.DropBoxManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.variableBuilder.GeneratedProviderInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class InjectionNodeBuilderRepositoryFactory {

    private Map<ASTType, InjectionNodeBuilder> moduleConfiguration = new HashMap<ASTType, InjectionNodeBuilder>();
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private Map<String, Class<?>> systemService;
    private Provider<GeneratedProviderInjectionNodeBuilder> generatedProviderInjectionNodeBuilderProvider;
    private ASTClassFactory astClassFactory;
    private InjectionBindingBuilder injectionBindingBuilder;

    @Inject
    public InjectionNodeBuilderRepositoryFactory(VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                                 Provider<GeneratedProviderInjectionNodeBuilder> generatedProviderInjectionNodeBuilderProvider,
                                                 ASTClassFactory astClassFactory, InjectionBindingBuilder injectionBindingBuilder) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.generatedProviderInjectionNodeBuilderProvider = generatedProviderInjectionNodeBuilderProvider;
        this.astClassFactory = astClassFactory;
        this.injectionBindingBuilder = injectionBindingBuilder;

        systemService = new HashMap<String, Class<?>>();
        systemService.put(Context.ACCESSIBILITY_SERVICE, AccessibilityManager.class);
        systemService.put(Context.ACCOUNT_SERVICE, AccountManager.class);
        systemService.put(Context.ACTIVITY_SERVICE, ActivityManager.class);
        systemService.put(Context.ALARM_SERVICE, AlarmManager.class);
        systemService.put(Context.AUDIO_SERVICE, AudioManager.class);
        systemService.put(Context.CLIPBOARD_SERVICE, ClipboardManager.class);
        systemService.put(Context.CONNECTIVITY_SERVICE, ConnectivityManager.class);
        systemService.put(Context.DEVICE_POLICY_SERVICE, DevicePolicyManager.class);
        //systemService.put(Context.DOWNLOAD_SERVICE, DownloadManager.class);
        systemService.put(Context.DROPBOX_SERVICE, DropBoxManager.class);
        systemService.put(Context.INPUT_METHOD_SERVICE, InputMethodManager.class);
        systemService.put(Context.NOTIFICATION_SERVICE, NotificationManager.class);
        systemService.put(Context.KEYGUARD_SERVICE, KeyguardManager.class);
        systemService.put(Context.LAYOUT_INFLATER_SERVICE, LayoutInflater.class);
        systemService.put(Context.LOCATION_SERVICE, LocationManager.class);
        //systemService.put(Context.NFC_SERVICE, NfcManager.class);
        systemService.put(Context.NOTIFICATION_SERVICE, NotificationManager.class);
        systemService.put(Context.POWER_SERVICE, PowerManager.class);
        systemService.put(Context.SEARCH_SERVICE, SearchManager.class);
        systemService.put(Context.SENSOR_SERVICE, SensorManager.class);
        //systemService.put(Context.STORAGE_SERVICE, StorageManager.class);
        systemService.put(Context.TELEPHONY_SERVICE, TelephonyManager.class);
        //systemService.put(Context.TEXT_SERVICES_MANAGER_SERVICE, TextServicesManager.class);
        systemService.put(Context.UI_MODE_SERVICE, UiModeManager.class);
        //systemService.put(Context.USB_SERVICE, UsbManager.class);
        systemService.put(Context.VIBRATOR_SERVICE, Vibrator.class);
        systemService.put(Context.WALLPAPER_SERVICE, WallpaperService.class);
        //systemService.put(Context.WIFI_P2P_SERVICE, WifiP2pManager.class);
        systemService.put(Context.WIFI_SERVICE, WifiManager.class);
        systemService.put(Context.WINDOW_SERVICE, WindowManager.class);
    }

    public void addApplicationInjections(InjectionNodeBuilderRepository repository){
        //system services
        for (Map.Entry<String, Class<?>> systemServiceEntry : systemService.entrySet()) {
            repository.putType(systemServiceEntry.getValue(),
                    injectionBindingBuilder.dependency(Context.class).invoke("getSystemService").arg(systemServiceEntry.getKey()).build());
        }


        repository.putType(SharedPreferences.class,
                injectionBindingBuilder.staticInvoke(PreferenceManager.class, "getDefaultSharedPreferences").depenencyArg(Context.class).build());

        //provider type
        repository.putType(Provider.class, generatedProviderInjectionNodeBuilderProvider.get());

        for (Map.Entry<ASTType, InjectionNodeBuilder> astTypeInjectionNodeBuilderEntry : moduleConfiguration.entrySet()) {
            repository.putType(astTypeInjectionNodeBuilderEntry.getKey(), astTypeInjectionNodeBuilderEntry.getValue());
        }
    }

    public void putModuleConfig(ASTType type, InjectionNodeBuilder injectionNodeBuilder){
        moduleConfiguration.put(type, injectionNodeBuilder);
    }
}
