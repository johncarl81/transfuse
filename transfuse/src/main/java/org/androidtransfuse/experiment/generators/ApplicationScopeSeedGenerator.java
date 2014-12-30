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
package org.androidtransfuse.experiment.generators;

import com.sun.codemodel.*;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.scope.ApplicationScope;
import org.androidtransfuse.scope.ScopeKey;
import org.androidtransfuse.scope.Scopes;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ApplicationScopeSeedGenerator implements Generation {

    private final ASTElementFactory astElementFactory;
    private final UniqueVariableNamer namer;
    private final ClassGenerationUtil generationUtil;

    @Inject
    public ApplicationScopeSeedGenerator(UniqueVariableNamer namer, ASTElementFactory astElementFactory, ClassGenerationUtil generationUtil) {
        this.namer = namer;
        this.astElementFactory = astElementFactory;
        this.generationUtil = generationUtil;
    }

    @Override
    public void schedule(final ComponentBuilder builder, ComponentDescriptor descriptor) {
        ASTMethod onCreateMethod = astElementFactory.findMethod(AndroidLiterals.APPLICATION, "onCreate");
        builder.add(onCreateMethod, GenerationPhase.POSTSCOPES, new ComponentMethodGenerator() {
            @Override
            public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                // get ApplicationScope
                JClass applicationScopeType = generationUtil.ref(ApplicationScope.class);
                JVar scopeVar = block.decl(applicationScopeType, namer.generateName(ApplicationScope.class),
                        JExpr.cast(applicationScopeType, builder.getScopes().invoke(Scopes.GET_SCOPE)
                                .arg(generationUtil.ref(ApplicationScope.ApplicationScopeQualifier.class).dotclass()))
                );

                //seed Application to "this"
                block.invoke(scopeVar, ApplicationScope.SEED_METHOD)
                        .arg(buildScopeKey(AndroidLiterals.APPLICATION))
                        .arg(JExpr._this());

                block.invoke(scopeVar, ApplicationScope.SEED_METHOD)
                        .arg(buildScopeKey(AndroidLiterals.CONTEXT))
                        .arg(JExpr._this());
            }
        });
    }

    private JInvocation buildScopeKey(ASTType target){

        JClass injectionNodeClassRef = generationUtil.ref(target);

        return generationUtil.ref(ScopeKey.class).staticInvoke(ScopeKey.GET_METHOD).arg(injectionNodeClassRef.dotclass()).arg(JExpr.lit(new InjectionSignature(target).buildScopeKeySignature()));
    }
}
