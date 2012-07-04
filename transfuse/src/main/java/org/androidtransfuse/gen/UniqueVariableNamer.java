package org.androidtransfuse.gen;

import com.sun.codemodel.JType;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class UniqueVariableNamer {

    private Map<String, Integer> nameMap = new HashMap<String, Integer>();

    public String generateName(Class clazz) {
        return generateName(clazz.getName(), true);
    }

    public String generateName(ASTType astType) {
        return generateName(astType.getName(), true);
    }

    public String generateName(JType definedClass) {
        return generateName(definedClass.fullName(), true);
    }

    public String generateName(InjectionNode injectionNode) {
        return generateName(injectionNode.getClassName(), true);
    }

    public String generateClassName(Class clazz) {
        return generateName(clazz.getName(), false);
    }

    public String generateClassName(ASTType astType) {
        return generateName(astType.getName(), false);
    }

    public String generateClassName(JType definedClass) {
        return generateName(definedClass.fullName(), false);
    }

    public String generateClassName(InjectionNode injectionNode) {
        return generateName(injectionNode.getClassName(), false);
    }

    private synchronized String generateName(String fullClassName, boolean lowerFirst) {

        //remove array notation
        String sanitizedFullClassName = fullClassName.replaceAll("\\[\\]", "");

        if (!nameMap.containsKey(sanitizedFullClassName)) {
            nameMap.put(sanitizedFullClassName, 0);
        } else {
            nameMap.put(sanitizedFullClassName, nameMap.get(sanitizedFullClassName) + 1);
        }

        String className = sanitizedFullClassName;
        if (fullClassName.contains(".")) {
            className = sanitizedFullClassName.substring(sanitizedFullClassName.lastIndexOf('.') + 1);
        }

        // build class name with the following format:
        // <lower case> + classname + _ + #
        StringBuilder builder = new StringBuilder();

        if(lowerFirst){
            if (!className.isEmpty()) {
                builder.append(Character.toLowerCase(className.charAt(0)));
            }
            if (className.length() > 1) {
                builder.append(className.substring(1));
            }
        }
        else{
            builder.append(className);
        }
        builder.append('_');
        builder.append(nameMap.get(sanitizedFullClassName));
        return builder.toString();
    }
}
