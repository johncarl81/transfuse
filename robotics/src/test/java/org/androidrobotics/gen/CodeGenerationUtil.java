package org.androidrobotics.gen;

import com.sun.codemodel.JCodeModel;
import org.androidrobotics.gen.classloader.MemoryClassLoader;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class CodeGenerationUtil {

    @Inject
    private JCodeModel codeModel;
    @Inject
    private StringCodeWriter stringCodeWriter;
    @Inject
    private MemoryClassLoader classLoader;

    public ClassLoader build(boolean print) throws IOException {
        codeModel.build(stringCodeWriter);

        classLoader.add(stringCodeWriter.getOutput());

        if (print) {
            for (Map.Entry<String, String> codeEntry : stringCodeWriter.getOutput().entrySet()) {
                System.out.println("Key: " + codeEntry.getKey());
                System.out.println(codeEntry.getValue());
            }
        }

        return classLoader;
    }
}