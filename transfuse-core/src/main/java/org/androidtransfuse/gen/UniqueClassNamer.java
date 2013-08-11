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
package org.androidtransfuse.gen;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.util.GeneratedCodeRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author John Ericksen
 */
@Singleton
public class UniqueClassNamer {

    private static final Map<String, String> PROHIBITED_PACKAGES = new HashMap<String, String>();

    static{
        PROHIBITED_PACKAGES.put("java.", "java_.");
        PROHIBITED_PACKAGES. put("javax.", "javax_.");
    }

    private final ConcurrentMap<String, AtomicInteger> nameMap = new ConcurrentHashMap<String, AtomicInteger>();
    private final String namespace;

    @Inject
    public UniqueClassNamer(){
        this(null);
    }

    public UniqueClassNamer(String namespace) {
        this.namespace = namespace;
    }

    public class ClassNameBuilder{
        private PackageClass packageClass;
        private boolean namespaced;
        private String appendment;
        private boolean numbered = true;

        public ClassNameBuilder(PackageClass packageClass) {
            this.packageClass = packageClass;
        }

        public ClassNameBuilder namespaced(){
            this.namespaced = true;
            return this;
        }

        public ClassNameBuilder append(String appendment) {
            this.appendment = appendment;
            return this;
        }

        public ClassNameBuilder setNumbered(boolean numbered){
            this.numbered = numbered;
            return this;
        }

        public PackageClass build(){

            PackageClass input = packageClass;
            if(appendment != null){
                input = input.append(GeneratedCodeRepository.SEPARATOR + appendment);
            }

            if(namespaced && namespace != null){
                input = input.prepend(namespace + GeneratedCodeRepository.SEPARATOR);
            }

            return new PackageClass(getPackage(input.getPackage()), generateName(input.getClassName(), numbered));
        }
    }

    public ClassNameBuilder generateClassName(PackageClass packageClass) {
        return new ClassNameBuilder(packageClass);
    }

    public ClassNameBuilder generateClassName(Class clazz) {
        return generateClassName(new PackageClass(clazz));
    }

    public ClassNameBuilder generateClassName(ASTType astType) {
        return generateClassName(astType.getPackageClass());
    }

    public ClassNameBuilder generateClassName(JDefinedClass definedClass) {
        return generateClassName(new PackageClass(definedClass._package().name(), definedClass.name()));
    }

    public ClassNameBuilder generateClassName(InjectionNode injectionNode) {
        return generateClassName(injectionNode.getASTType());
    }

    public String getPackage(String inputPackage){
        if(inputPackage == null){
            return null;
        }
        // Avoid prohibited package names
        String validPackage = inputPackage;
        for (Map.Entry<String, String> prohibitedPackage : PROHIBITED_PACKAGES.entrySet()) {
            if(inputPackage.startsWith(prohibitedPackage.getKey())){
                validPackage = inputPackage.replaceFirst(prohibitedPackage.getKey(), prohibitedPackage.getValue());
            }
        }
        return validPackage;
    }

    private String generateName(String fullClassName, boolean numbered) {

        //remove array notation
        String sanitizedFullClassName = fullClassName.replaceAll("\\[\\]", "");

        String className = sanitizedFullClassName;
        if (fullClassName.contains(".")) {
            className = sanitizedFullClassName.substring(sanitizedFullClassName.lastIndexOf('.') + 1);
        }

        // build class name with the following format:
        // <lower case> + classname + _ + #
        StringBuilder builder = new StringBuilder();

        builder.append(className);

        String nameRoot = builder.toString();

        if(numbered){
            builder.append(GeneratedCodeRepository.SEPARATOR);
            builder.append(nullSafeIterGet(nameRoot));
        }
        return builder.toString();
    }

    private int nullSafeIterGet(String name){
        AtomicInteger result = nameMap.get(name);
        if (result == null) {
            AtomicInteger value = new AtomicInteger();
            result = nameMap.putIfAbsent(name, value);
            if (result == null) {
                result = value;
            }
        }

        return result.getAndIncrement();
    }
}
