package org.androidrobotics;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.ActivityAnalysis;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.ActivityGenerator;
import org.androidrobotics.model.ActivityDescriptor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Collection;

/**
 * @author John Ericksen
 */
@Singleton
public class RoboticsProcessor {

    @Inject
    private ActivityAnalysis activityAnalysis;
    @Inject
    private ActivityGenerator activityGenerator;
    @Inject
    private JCodeModel codeModel;

    public void processModuleElements(Collection<? extends ASTType> astTypes) {
        for (ASTType astType : astTypes) {
            //todo: module configuration
        }
    }

    public void processRootElement(Collection<? extends ASTType> astTypes) {

        for (ASTType astType : astTypes) {
            ActivityDescriptor activityDescriptor = activityAnalysis.analyzeElement(astType);

            if (activityDescriptor != null) {
                try {
                    activityGenerator.generate(activityDescriptor);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JClassAlreadyExistsException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void verify() {

    }

    public void writeSource(CodeWriter codeWriter, CodeWriter recourceWriter) {

        try {
            codeModel.build(
                    codeWriter,
                    recourceWriter);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
