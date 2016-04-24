/**
 * Copyright 2014-2015 John Ericksen
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
package org.rbridge;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTField;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.ClassNamer;
import org.androidtransfuse.transaction.AbstractCompletionTransactionWorker;
import org.androidtransfuse.util.Logger;
import org.androidtransfuse.util.TransfuseRuntimeException;

import javax.inject.Inject;
import javax.inject.Provider;

import static com.sun.codemodel.JMod.*;

/**
 * @author John Ericksen
 */
public class RBridgeWorker extends AbstractCompletionTransactionWorker<Provider<ASTType>, JDefinedClass> {

    private final ClassGenerationUtil generationUtil;
    private final JCodeModel codeModel;
    private final Logger log;
    private int id = -1;

    @Inject
    public RBridgeWorker(ClassGenerationUtil generationUtil,
                         JCodeModel codeModel, Logger log) {
        this.generationUtil = generationUtil;
        this.codeModel = codeModel;
        this.log = log;
    }

    @Override
    public JDefinedClass innerRun(Provider<ASTType> astTypeProvider) {
        ASTType implementation = astTypeProvider.get();
        log.debug("Processing @Bridge on " + implementation);
        ASTAnnotation bridgeAnnotation = implementation.getASTAnnotation(Bridge.class);
        ASTType target = bridgeAnnotation.getProperty("value", ASTType.class);

        try {
            log.debug("Creating Bridge for " + target);
            JDefinedClass rBridgeImpl = generationUtil.defineClass(ClassNamer.className(target).build().append("Bridge"));

            for (ASTType innerType : target.getInnerTypes()) {

                String innerName = innerType.getPackageClass().getCanonicalName();
                if(innerName.contains(".")) {
                    innerName = innerName.substring(innerName.lastIndexOf(".") + 1);
                }
                JDefinedClass innerBridge = rBridgeImpl._class(PUBLIC | STATIC | FINAL, innerName);

                for (ASTField field : innerType.getFields()) {
                    log.debug("Mapping " + target + "." + innerName + "." + field.getName() + " = " + id);

                    innerBridge.field(PUBLIC | STATIC | FINAL, codeModel.INT, field.getName(), JExpr.lit(id--))
                        .annotate(RMapping.class)
                            .param("clazz", generationUtil.ref(innerType).dotclass())
                            .param("name", field.getName());

                }
            }

            return rBridgeImpl;
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseRuntimeException("Class Already exists", e);
        }
    }
}
