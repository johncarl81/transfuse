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
package org.androidtransfuse.experiment.generators;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpr;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.annotations.WindowFeature;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class WindowFeatureGenerator implements Generation {

    private final ASTElementFactory astElementFactory;

    @Inject
    public WindowFeatureGenerator(ASTElementFactory astElementFactory) {
        this.astElementFactory = astElementFactory;
    }

    @Override
    public void schedule(ComponentBuilder builder, ComponentDescriptor descriptor) {
        ASTType target = descriptor.getTarget();
        if (target.isAnnotated(WindowFeature.class)) {
            ASTAnnotation windowFeatureAnnotation = target.getASTAnnotation(WindowFeature.class);
            final Integer[] values = windowFeatureAnnotation.getProperty("value", Integer[].class);

            builder.add(astElementFactory.findMethod(AndroidLiterals.ACTIVITY, "onCreate", AndroidLiterals.BUNDLE), GenerationPhase.LAYOUT, new ComponentMethodGenerator() {
                @Override
                public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                    for (int value : values) {
                        block.invoke("requestWindowFeature").arg(JExpr.lit(value));
                    }
                }
            });
        }
    }
}
