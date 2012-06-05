package org.androidtransfuse.gen;

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
        return generateName(clazz.getName());
    }

    public synchronized String generateName(String fullClassName) {

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

        if (!className.isEmpty()) {
            builder.append(Character.toLowerCase(className.charAt(0)));
        }
        if (className.length() > 1) {
            builder.append(className.substring(1));
        }
        builder.append('_');
        builder.append(nameMap.get(sanitizedFullClassName));
        return builder.toString();
    }
}
