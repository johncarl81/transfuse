/**
 * Copyright 2011-2015 John Ericksen
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
package org.androidtransfuse.analysis.astAnalyzer;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.model.Aspect;
import org.androidtransfuse.model.InjectionNodeLogger;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ManualSuperAspect implements Aspect {

    private Set<Method> methods = new HashSet<Method>();

    public void add(String name, ImmutableList<ASTType> parameters) {
        methods.add(new Method(name, parameters));
    }

    public Set<Method> getMethods() {
        return methods;
    }

    public void add(ASTMethod eventMethod) {
        add(eventMethod.getName(), FluentIterable.from(eventMethod.getParameters())
                .transform(new Function<ASTParameter, ASTType>() {
                    public ASTType apply(ASTParameter parameter) {
                        return parameter.getASTType();
                    }
                }).toList());
    }

    public class Method{

        private final String name;
        private final ImmutableList<ASTType> parameters;

        public Method(String name, ImmutableList<ASTType> parameters) {
            this.name = name;
            this.parameters = parameters;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Method)) {
            return false;
        }
            if (this == obj) {
                return true;
            }
            Method that = (Method) obj;
            return new EqualsBuilder().append(name, that.name).append(parameters, that.parameters).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(name).append(parameters).hashCode();
        }

        public ImmutableList<ASTType> getParameters() {
            return parameters;
        }

        public String getName() {
            return name;
        }
    }

    @Override
    public void log(InjectionNodeLogger logger) {
        logger.append("ManualSuperAspect{");
        logger.pushIndent();
        for (Method method : methods) {
            logger.append("\n");
            logger.append(method.getName());
        }
        logger.popIndent();
        logger.append("\n}");
    }

}
