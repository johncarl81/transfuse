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
package org.androidtransfuse.experiment;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JVar;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.annotations.Factory;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.ScopesGenerator;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.scope.Scopes;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ScopesGeneration implements PreInjectionGeneration{

    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer namer;
    private final ASTMethod method;

    @Inject
    public ScopesGeneration(ClassGenerationUtil generationUtil, UniqueVariableNamer namer, ASTMethod method) {
        this.generationUtil = generationUtil;
        this.namer = namer;
        this.method = method;
    }

    @Factory public interface ScopesGenerationFactory{
        ScopesGeneration build(ASTMethod method);
    }

    @Override
    public void schedule(final ComponentBuilder builder, ComponentDescriptor descriptor) {
        builder.add(method, GenerationPhase.SCOPES, new ComponentMethodGenerator() {
            @Override
            public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                // Scopes instance
                JClass scopes = generationUtil.ref(Scopes.class);
                JInvocation scopesBuildInvocation = generationUtil.ref(ScopesGenerator.TRANSFUSE_SCOPES_UTIL).staticInvoke(ScopesGenerator.GET_INSTANCE);
                JVar scopesVar = block.decl(scopes, namer.generateName(Scopes.class), scopesBuildInvocation);

                builder.setScopes(scopesVar);
            }
        });
    }
}
