package org.androidrobotics;

import android.accounts.AccountManager;
import android.app.*;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.DropBoxManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.service.wallpaper.WallpaperService;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.ActivityAnalysis;
import org.androidrobotics.analysis.AnalysisRepository;
import org.androidrobotics.analysis.AnalysisRepositoryFactory;
import org.androidrobotics.analysis.InterceptorRepository;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.annotations.Bind;
import org.androidrobotics.annotations.BindInterceptor;
import org.androidrobotics.annotations.RoboticsModule;
import org.androidrobotics.gen.ActivityGenerator;
import org.androidrobotics.gen.InjectionNodeBuilderRepository;
import org.androidrobotics.gen.VariableBuilderRepositoryFactory;
import org.androidrobotics.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.util.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class RoboticsProcessor {

    private ActivityGenerator activityGenerator;
    private JCodeModel codeModel;
    private Logger logger;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private AnalysisRepositoryFactory analysisRepositoryFactory;
    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private InterceptorRepository interceptorRepository;
    private ActivityAnalysis activityAnalysis;

    @Inject
    public RoboticsProcessor(ActivityGenerator activityGenerator,
                             JCodeModel codeModel,
                             Logger logger,
                             VariableBuilderRepositoryFactory variableBuilderRepositoryProvider,
                             VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                             AnalysisRepositoryFactory analysisRepositoryFactory,
                             ActivityAnalysis activityAnalysis,
                             Provider<InterceptorRepository> interceptorRepositoryProvider) {
        this.activityGenerator = activityGenerator;
        this.codeModel = codeModel;
        this.logger = logger;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.analysisRepositoryFactory = analysisRepositoryFactory;
        this.activityAnalysis = activityAnalysis;

        interceptorRepository = interceptorRepositoryProvider.get();
        injectionNodeBuilders = variableBuilderRepositoryProvider.buildRepository();

        //temporary
        Map<String, Class<?>> systemService = new HashMap<String, Class<?>>() {{
            put(Context.ACCESSIBILITY_SERVICE, AccessibilityManager.class);
            put(Context.ACCOUNT_SERVICE, AccountManager.class);
            put(Context.ACTIVITY_SERVICE, ActivityManager.class);
            put(Context.ALARM_SERVICE, AlarmManager.class);
            put(Context.AUDIO_SERVICE, AudioManager.class);
            put(Context.CLIPBOARD_SERVICE, ClipboardManager.class);
            put(Context.CONNECTIVITY_SERVICE, ConnectivityManager.class);
            put(Context.DEVICE_POLICY_SERVICE, DevicePolicyManager.class);
            //put(Context.DOWNLOAD_SERVICE, DownloadManager.class);
            put(Context.DROPBOX_SERVICE, DropBoxManager.class);
            put(Context.INPUT_METHOD_SERVICE, InputMethodManager.class);
            put(Context.NOTIFICATION_SERVICE, NotificationManager.class);
            put(Context.KEYGUARD_SERVICE, KeyguardManager.class);
            put(Context.LAYOUT_INFLATER_SERVICE, LayoutInflater.class);
            put(Context.LOCATION_SERVICE, LocationManager.class);
            //put(Context.NFC_SERVICE, NfcManager.class);
            put(Context.NOTIFICATION_SERVICE, NotificationManager.class);
            put(Context.POWER_SERVICE, PowerManager.class);
            put(Context.SEARCH_SERVICE, SearchManager.class);
            put(Context.SENSOR_SERVICE, SensorManager.class);
            //put(Context.STORAGE_SERVICE, StorageManager.class);
            put(Context.TELEPHONY_SERVICE, TelephonyManager.class);
            //put(Context.TEXT_SERVICES_MANAGER_SERVICE, TextServicesManager.class);
            put(Context.UI_MODE_SERVICE, UiModeManager.class);
            //put(Context.USB_SERVICE, UsbManager.class);
            put(Context.VIBRATOR_SERVICE, Vibrator.class);
            put(Context.WALLPAPER_SERVICE, WallpaperService.class);
            //put(Context.WIFI_P2P_SERVICE, WifiP2pManager.class);
            put(Context.WIFI_SERVICE, WifiManager.class);
            put(Context.WINDOW_SERVICE, WindowManager.class);
        }};

        for (Map.Entry<String, Class<?>> systemServiceEntry : systemService.entrySet()) {
            injectionNodeBuilders.put(systemServiceEntry.getValue().getName(),
                    variableInjectionBuilderFactory.buildSystemServiceInjectionNodeBuilder(
                            systemServiceEntry.getKey(),
                            systemServiceEntry.getValue()));
        }
    }

    public void processModuleElements(Collection<? extends ASTType> astTypes) {

        for (ASTType astType : astTypes) {
            if (astType.isAnnotated(RoboticsModule.class)) {

                for (ASTMethod astMethod : astType.getMethods()) {
                    if (astMethod.isAnnotated(Bind.class)) {
                        ASTType superType = astMethod.getReturnType();

                        if (astMethod.getParameters().size() == 1) {
                            ASTType implType = astMethod.getParameters().get(0).getASTType();

                            injectionNodeBuilders.put(superType.getName(),
                                    variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(implType));
                        } else {
                            //todo: throw exception
                        }
                    }

                    if (astMethod.isAnnotated(BindInterceptor.class)) {
                        ASTType interceptor = astMethod.getReturnType();

                        if (astMethod.getParameters().size() == 1) {
                            ASTType annotationType = astMethod.getParameters().get(0).getASTType();


                        } else {
                            //todo: throw exception
                        }
                    }
                }
            }
        }
    }

    public void processRootElement(Collection<? extends ASTType> astTypes) {

        AnalysisRepository analysisRepository = analysisRepositoryFactory.buildAnalysisRepository();

        for (ASTType astType : astTypes) {

            ActivityDescriptor activityDescriptor = activityAnalysis.analyzeElement(astType, analysisRepository, injectionNodeBuilders, interceptorRepository);

            if (activityDescriptor != null) {
                try {
                    activityGenerator.generate(activityDescriptor);
                } catch (IOException e) {
                    logger.error("IOException while generating activity", e);
                } catch (JClassAlreadyExistsException e) {
                    logger.error("IOException while generating activity", e);
                } catch (ClassNotFoundException e) {
                    logger.error("IOException while generating activity", e);
                }
            }
        }
    }

    public void verify() {

    }

    public void writeSource(CodeWriter codeWriter, CodeWriter resourceWriter) {

        try {
            codeModel.build(
                    codeWriter,
                    resourceWriter);

        } catch (IOException e) {
            logger.error("Error while writing source files", e);
        }
    }
}
