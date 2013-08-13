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

import com.sun.codemodel.*;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.analysis.module.ModuleRepository;
import org.androidtransfuse.scope.Scopes;
import org.androidtransfuse.util.Namer;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ScopesGenerator {

    public static final PackageClass TRANSFUSE_SCOPES_UTIL = new PackageClass("org.androidtransfuse", Namer.name("Transfuse").append("ScopesUtil").build());
    public static final String GET_INSTANCE = "getInstance";
    public static final String BUILD_METHOD = "build";

    private final ClassGenerationUtil generationUtil;
    private final ModuleRepository repository;
    private final UniqueVariableNamer namer;

    @Inject
    public ScopesGenerator(ClassGenerationUtil generationUtil, ModuleRepository repository, UniqueVariableNamer namer) {
        this.generationUtil = generationUtil;
        this.repository = repository;
        this.namer = namer;
    }

    public void generate(){

        try {
            JDefinedClass scopesUtil = generationUtil.defineClass(TRANSFUSE_SCOPES_UTIL);
            scopesUtil.mods().setFinal(true);

            //private utility constructor
            scopesUtil.constructor(JMod.PRIVATE);

            // builder method
            JMethod buildMethod = scopesUtil.method(JMod.PUBLIC | JMod.STATIC, Scopes.class, BUILD_METHOD);

            JBlock buildMethodBody = buildMethod.body();

            buildMethodBody._return(buildScopes(repository, generationUtil, namer, buildMethodBody));

            // static get instance method
            JFieldVar instance = scopesUtil.field(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, Scopes.class, "INSTANCE", JExpr.invoke(buildMethod));

            JMethod getInstanceMethod = scopesUtil.method(JMod.PUBLIC | JMod.STATIC, Scopes.class, GET_INSTANCE);
            getInstanceMethod.body()._return(instance);


        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Unable to build Transfuse$ScopesUtil class", e);
        }
    }

    public static JVar buildScopes(ModuleRepository repository, ClassGenerationUtil generationUtil, UniqueVariableNamer namer, JBlock injectorBlock) {
        JClass scopesRef = generationUtil.ref(Scopes.class);
        JVar scopesVar = injectorBlock.decl(scopesRef, namer.generateName(Scopes.class), JExpr._new(scopesRef));

        for (Map.Entry<ASTType, ASTType> scopeTypeEntry : repository.buildModuleConfiguration().getScopeAnnotations().entrySet()) {
            JExpression annotation = generationUtil.ref(scopeTypeEntry.getKey()).dotclass();
            JClass scopeType = generationUtil.ref(scopeTypeEntry.getValue());

            injectorBlock.invoke(scopesVar, Scopes.ADD_SCOPE).arg(annotation).arg(JExpr._new(scopeType));
        }

        return scopesVar;
    }
}
