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
package org.androidtransfuse.processor;

import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.config.TransfuseAndroidModule;
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.model.manifest.*;
import org.androidtransfuse.util.AndroidLiterals;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
@Singleton
public class ManifestManager {

    private final List<Application> applications = new ArrayList<Application>();
    private final String manifestPackage;
    private final List<Activity> activities = new ArrayList<Activity>();
    private final List<Receiver> broadcastReceivers = new ArrayList<Receiver>();
    private final List<Service> services = new ArrayList<Service>();
    private final List<UsesPermission> usesPermissions = new ArrayList<UsesPermission>();
    private final List<UsesFeature> usesFeatures = new ArrayList<UsesFeature>();
    private final List<Permission> permissions = new ArrayList<Permission>();
    private final Logger log;
    private UsesSDK usesSdk;

    @Inject
    public ManifestManager(@Named(TransfuseAndroidModule.ORIGINAL_MANIFEST) Manifest originalManifest, Logger log) {
        this.log = log;
        this.manifestPackage = originalManifest.getApplicationPackage();
    }

    public void addApplication(Application application) {
        log.debug("Adding to manifest: " + application);
        this.applications.add(application);
    }

    public void addPermission(Permission permission){
        try {
            log.debug("Adding to manifest: " + permission);
            updateMergeTags(Permission.class, permission);
            permissions.add(permission);
        } catch (MergerException e) {
            throw new TransfuseAnalysisException("Unable to Merge UsesPermission", e);
        }
    }

    public void addUsesFeature(UsesFeature usesFeature){
        try {
            log.debug("Adding to manifest: " + usesFeature);
            updateMergeTags(UsesFeature.class, usesFeature);
            usesFeatures.add(usesFeature);
        } catch (MergerException e) {
            throw new TransfuseAnalysisException("Unable to Merge UsesFeature", e);
        }
    }

    public void addUsesPermission(UsesPermission usesPermission) {
        try {
            log.debug("Adding to manifest: " + usesPermission);
            updateMergeTags(UsesPermission.class, usesPermission);
            usesPermissions.add(usesPermission);
        } catch (MergerException e) {
            throw new TransfuseAnalysisException("Unable to Merge UsesPermission", e);
        }
    }

    public void setUsesSdk(UsesSDK usesSdk) {
        log.debug("Adding to manifest: " + usesSdk);
        this.usesSdk = usesSdk;
    }

    public void addActivity(Activity activity) {
        try {
            log.debug("Adding to manifest: " + activity);
            updateMergeTags(Activity.class, activity);
            updateMergeTags(IntentFilter.class, activity.getIntentFilters());
            updateMergeTags(MetaData.class, activity.getMetaData());
            this.activities.add(activity);
        } catch (MergerException e) {
            throw new TransfuseAnalysisException("Unable to Merge Activity", e);
        }
    }

    public void addBroadcastReceiver(Receiver broadcastReceiver) {
        try {
            log.debug("Adding to manifest: " + broadcastReceiver);
            updateMergeTags(Receiver.class, broadcastReceiver);
            updateMergeTags(IntentFilter.class, broadcastReceiver.getIntentFilters());
            updateMergeTags(MetaData.class, broadcastReceiver.getMetaData());
            this.broadcastReceivers.add(broadcastReceiver);
        } catch (MergerException e) {
            throw new TransfuseAnalysisException("Unable to Merge Broadcast Receiver", e);
        }
    }

    private <T extends Mergeable> void updateMergeTags(Class<T> clazz, List<T> mergeableCollection) throws MergerException {
        for (T mergeable : mergeableCollection) {
            updateMergeTags(clazz, mergeable);
        }
    }

    public void addService(Service service) {
        try {
            log.debug("Adding to manifest: " + service);
            updateMergeTags(Service.class, service);
            updateMergeTags(IntentFilter.class, service.getIntentFilters());
            updateMergeTags(MetaData.class, service.getMetaData());
            this.services.add(service);
        } catch (MergerException e) {
            throw new TransfuseAnalysisException("Unable to Merge Service", e);
        }
    }

    public Manifest getManifest() throws MergerException {
        Manifest manifest = new Manifest();
        manifest.setApplicationPackage(manifestPackage);

        Application localApplication;

        if (applications.isEmpty()){
            localApplication = new Application();
            localApplication.setName(AndroidLiterals.APPLICATION.getName());
        }
        else{
            localApplication = applications.get(0);
        }

        //todo: do multiple applications result in an error?

        updateMergeTags(Application.class, localApplication);

        localApplication.getActivities().addAll(activities);
        localApplication.getReceivers().addAll(broadcastReceivers);
        localApplication.getServices().addAll(services);

        manifest.getApplications().add(localApplication);

        manifest.getUsesPermissions().addAll(usesPermissions);

        manifest.getUsesFeatures().addAll(usesFeatures);

        manifest.getPermissions().addAll(permissions);

        if(usesSdk != null){
            manifest.getUsesSDKs().add(usesSdk);
        }

        manifest.updatePackages();

        return manifest;
    }

    private <T extends Mergeable> void updateMergeTags(Class<T> clazz, T mergeable) throws MergerException {
        try {
            mergeable.setGenerated(true);

            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);

            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                Method readMethod = propertyDescriptor.getReadMethod();
                Method writeMethod = propertyDescriptor.getWriteMethod();

                Merge mergeAnnotation = findAnnotation(Merge.class, writeMethod, readMethod);
                if (mergeAnnotation != null) {
                    mergeable.addMergeTag(mergeAnnotation.value());
                }
            }
        } catch (IntrospectionException e) {
            throw new MergerException(e);
        }
    }

    private <T extends Annotation> T findAnnotation(Class<T> annotationClass, Method... methods) {
        T annotation = null;
        if (methods != null) {
            for (Method method : methods) {
                if (annotation == null && method != null && method.isAnnotationPresent(annotationClass)) {
                    annotation = method.getAnnotation(annotationClass);
                }
            }
        }
        return annotation;
    }
}
