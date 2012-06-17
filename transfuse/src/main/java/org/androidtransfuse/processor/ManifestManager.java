package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.config.TransfuseGenerateGuiceModule;
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.model.manifest.*;
import org.apache.commons.beanutils.PropertyUtils;

import javax.inject.Inject;
import javax.inject.Named;
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
    private String manifestPackage;
    private List<Activity> activities = new ArrayList<Activity>();
    private List<Receiver> broadcastReceivers = new ArrayList<Receiver>();
    private List<Service> services = new ArrayList<Service>();

    @Inject
    public ManifestManager(@Named(TransfuseGenerateGuiceModule.ORIGINAL_MANIFEST) Manifest originialManifest){
        this.manifestPackage = originialManifest.getApplicationPackage();
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void addActivity(Activity activity) {
        try {
            updateMergeTags(Activity.class, activity);
            updateMergeTags(IntentFilter.class, activity.getIntentFilters());
            updateMergeTags(MetaData.class, activity.getMetaData());
            this.activities.add(activity);
        } catch (MergerException e) {
            throw new TransfuseAnalysisException("Unable to Merge Service", e);
        }
    }

    public void addBroadcastReceiver(Receiver broadcastReceiver) {
        try {
            updateMergeTags(Receiver.class, broadcastReceiver);
            updateMergeTags(IntentFilter.class, broadcastReceiver.getIntentFilters());
            updateMergeTags(MetaData.class, broadcastReceiver.getMetaData());
            this.broadcastReceivers.add(broadcastReceiver);
        } catch (MergerException e) {
            throw new TransfuseAnalysisException("Unable to Merge Service", e);
        }
    }

    private <T extends Mergeable> void updateMergeTags(Class<T> clazz, List<T> mergableCollection) throws MergerException {
        for (T mergeable : mergableCollection) {
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
        manifest.setApplicationPackage(manifestPackage);

        Application localApplication = application;

        if (application == null) {
            localApplication = new Application();
            localApplication.setName(android.app.Application.class.getName());
        }

        updateMergeTags(Application.class, localApplication);

        localApplication.getActivities().addAll(activities);
        localApplication.getReceivers().addAll(broadcastReceivers);
        localApplication.getServices().addAll(services);

        manifest.getApplications().add(localApplication);

        manifest.updatePackages();

        return manifest;
    }

    private <T extends Mergeable> void updateMergeTags(Class<T> clazz, T mergeable) throws MergerException {
        try {
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
