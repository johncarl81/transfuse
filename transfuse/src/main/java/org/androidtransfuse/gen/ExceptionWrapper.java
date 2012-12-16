/**
 * Copyright 2012 John Ericksen
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
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.util.TransfuseInjectionException;

import javax.inject.Inject;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ExceptionWrapper {

    private final JCodeModel codeModel;

    @Inject
    public ExceptionWrapper(JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    public <T> T wrapException(JBlock block, List<ASTType> throwsTypes, BlockWriter<T> blockWriter) throws ClassNotFoundException, JClassAlreadyExistsException {
        JTryBlock tryBlock = null;
        JBlock writeBlock = block;
        if (throwsTypes.size() > 0) {
            tryBlock = block._try();
            writeBlock = tryBlock.body();
        }

        T output = blockWriter.write(writeBlock);

        if (tryBlock != null) {
            for (ASTType throwsType : throwsTypes) {
                JCatchBlock catchBlock = tryBlock._catch(codeModel.ref(throwsType.getName()));
                JVar exceptionParam = catchBlock.param("e");

                catchBlock.body()._throw(JExpr._new(codeModel.ref(TransfuseInjectionException.class))
                        .arg(JExpr.lit(throwsType.getName() + " while performing dependency injection"))
                        .arg(exceptionParam));
            }
        }
        return output;
    }

    public interface BlockWriter<T> {
        T write(JBlock block) throws ClassNotFoundException, JClassAlreadyExistsException;
    }
}
