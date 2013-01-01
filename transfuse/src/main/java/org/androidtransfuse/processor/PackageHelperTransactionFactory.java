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
package org.androidtransfuse.processor;

import org.androidtransfuse.config.EnterableScope;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class PackageHelperTransactionFactory {

    private final EnterableScope codeGenerationScope;
    private final Provider<TransactionWorker<Void, Void>> packageHelperGeneratorProvider;

    @Inject
    public PackageHelperTransactionFactory(
            @Named(TransfuseSetupGuiceModule.CODE_GENERATION_SCOPE) EnterableScope codeGenerationScope,
            @Named(TransfuseSetupGuiceModule.PACKAGE_HELPER_TRANSACTION_WORKER)
            Provider<TransactionWorker<Void, Void>> packageHelperGeneratorProvider) {
        this.packageHelperGeneratorProvider = packageHelperGeneratorProvider;
        this.codeGenerationScope = codeGenerationScope;
    }

    public Transaction<Void, Void> buildTransaction() {
        return new Transaction<Void, Void>(
                new ScopedTransactionWorker<Void, Void>(codeGenerationScope, packageHelperGeneratorProvider)
        );
    }
}
