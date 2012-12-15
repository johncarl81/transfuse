package org.androidtransfuse.analysis.module;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ModuleConfigurationComposite implements ModuleConfiguration {

    private List<ModuleConfiguration> moduleConfigurations = new ArrayList<ModuleConfiguration>();

    @Override
    public void setConfiguration() {
        for (ModuleConfiguration moduleConfiguration : moduleConfigurations) {
            moduleConfiguration.setConfiguration();
        }
    }

    public void add(ModuleConfiguration configuration){
        this.moduleConfigurations.add(configuration);
    }
}
