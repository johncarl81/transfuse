package org.androidrobotics.gen;

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
        if (!nameMap.containsKey(fullClassName)) {
            nameMap.put(fullClassName, 0);
        } else {
            nameMap.put(fullClassName, nameMap.get(fullClassName) + 1);
        }

        String className = fullClassName;
        if (fullClassName.contains(".")) {
            className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
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
        builder.append(nameMap.get(fullClassName));
        return builder.toString();
    }
}
