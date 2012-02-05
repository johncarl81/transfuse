package org.androidtransfuse.model;

import org.androidtransfuse.model.manifest.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ApplicationDescriptor {

    private PackageClass packageClass;
    private List<InjectionNode> injectionNodes = new ArrayList<InjectionNode>();
    private String label;
    private Application manifestApplication;

    public PackageClass getPackageClass() {
        return packageClass;
    }

    public void setPackageClass(PackageClass packageClass) {
        this.packageClass = packageClass;
    }

    public void addInjectionNode(InjectionNode injectionNode) {
        injectionNodes.add(injectionNode);
    }

    public List<InjectionNode> getInjectionNodes() {
        return injectionNodes;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setManifestApplication(Application manifestApplication) {
        this.manifestApplication = manifestApplication;
    }

    public Application getManifestApplication() {
        return manifestApplication;
    }
}
