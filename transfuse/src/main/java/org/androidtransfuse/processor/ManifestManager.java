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
package org.androidtransfuse.processor;

import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.model.manifest.*;
import org.androidtransfuse.util.AndroidLiterals;
import org.apache.commons.beanutils.PropertyUtils;

import javax.inject.Singleton;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
@Singleton
public class ManifestManager {

    private Application application;
    private final List<Activity> activities = new ArrayList<Activity>();
    private final List<Receiver> broadcastReceivers = new ArrayList<Receiver>();
    private final List<Service> services = new ArrayList<Service>();
    private final List<UsesPermission> usesPermissions = new ArrayList<UsesPermission>();
    private final List<UsesFeature> usesFeatures = new ArrayList<UsesFeature>();
    private final List<Permission> permissions = new ArrayList<Permission>();
    private UsesSDK usesSdk;

    public void setApplication(Application application) {
        this.application = application;
    }

    public void addPermission(Permission permission){
        try {
            updateMergeTags(Permission.class, permission);
            permissions.add(permission);
        } catch (MergerException e) {
            throw new TransfuseAnalysisException("Unable to Merge UsesPermission", e);
        }
    }
    
    public void addUsesFeature(UsesFeature usesFeature){
        try {
            updateMergeTags(UsesFeature.class, usesFeature);
            usesFeatures.add(usesFeature);
        } catch (MergerException e) {
            throw new TransfuseAnalysisException("Unable to Merge UsesFeature", e);
        }
    }

    public void addUsesPermission(String permission) {
        try {
            UsesPermission usesPermission = new UsesPermission(permission);
            updateMergeTags(UsesPermission.class, usesPermission);
            usesPermissions.add(usesPermission);
        } catch (MergerException e) {
            throw new TransfuseAnalysisException("Unable to Merge UsesPermission", e);
        }
    }

    public void setUsesSdk(UsesSDK usesSdk) {
        this.usesSdk = usesSdk;
    }

    public void addActivity(Activity activity) {
        try {
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

        Application localApplication = application;

        if (application == null) {
            localApplication = new Application();
            localApplication.setName(AndroidLiterals.APPLICATION.getName());
        }

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
                Object property = PropertyUtils.getProperty(mergeable, propertyDescriptor.getName());

                if (mergeAnnotation != null && property != null) {
                    mergeable.addMergeTag(mergeAnnotation.value());
                }
            }
        } catch (IntrospectionException e) {
            throw new MergerException(e);
        } catch (InvocationTargetException e) {
            throw new MergerException(e);
        } catch (NoSuchMethodException e) {
            throw new MergerException(e);
        } catch (IllegalAccessException e) {
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
