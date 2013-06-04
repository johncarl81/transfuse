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
package org.androidtransfuse.analysis.astAnalyzer;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.astAnalyzer.validation.AnnotationValidator;
import org.androidtransfuse.analysis.astAnalyzer.validation.AnnotationValidatorBuilder;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * @author John Ericksen
 */
public class AnnotationValidationAnalysis implements ASTAnalysis {

    private final AnnotationValidator annotationValidator;

    @Inject
    public AnnotationValidationAnalysis(AnnotationValidatorBuilder builder) {

        //built in specialty qualifiers
        builder.given(Extra.class).requires(Inject.class, "@Extra annotation must be accompanied by @Inject");
        builder.given(Extra.class).parameterMatches("value", "^[a-zA-Z][a-zA-Z0-9_]*$", "@Extra value parameter must follow Java Bean syntax");
        builder.given(View.class).requires(Inject.class, "@View annotation must be accompanied by @Inject");
        builder.given(Preference.class).requires(Inject.class, "@Preference annotation must be accompanied by @Inject");
        builder.given(Resource.class).requires(Inject.class, "@Resource annotation must be accompanied by @Inject");
        builder.given(SystemService.class).requires(Inject.class, "@SystemService annotation must be accompanied by @Inject");

        //activity metadata
        builder.given(Layout.class).requires(Arrays.asList(Activity.class, Fragment.class), "@Layout annotation must be accompanied by @Activity");
        builder.given(LayoutHandler.class).requires(Arrays.asList(Activity.class, Fragment.class), "@LayoutHandler annotation must be accompanied by @Activity");
        builder.given(MetaData.class).requires(Activity.class, "@MetaData annotation must be accompanied by @Activity");
        builder.given(MetaDataSet.class).requires(Activity.class, "@MetaDataSet annotation must be accompanied by @Activity");
        builder.given(Activity.class).parameterMatches("name", "^[a-zA-Z][a-zA-Z0-9_]*$", "@Activity name parameter must follow Java Bean syntax");
        builder.given(Service.class).parameterMatches("name", "^[a-zA-Z][a-zA-Z0-9_]*$", "@Service name parameter must follow Java Bean syntax");
        builder.given(Fragment.class).parameterMatches("name", "^[a-zA-Z][a-zA-Z0-9_]*$", "@Fragment name parameter must follow Java Bean syntax");
        builder.given(BroadcastReceiver.class).parameterMatches("name", "^[a-zA-Z][a-zA-Z0-9_]*$", "@BroadcastReceiver name parameter must follow Java Bean syntax");
        builder.given(Application.class).parameterMatches("name", "^[a-zA-Z][a-zA-Z0-9_]*$", "@Application name parameter must follow Java Bean syntax");

        //module metadata
        builder.given(Bind.class).requires(TransfuseModule.class, "@Bind annotation must be accompanied by @TransfuseModule");
        builder.given(Bindings.class).requires(TransfuseModule.class, "@Bindings annotation must be accompanied by @TransfuseModule");
        builder.given(BindInterceptor.class).requires(TransfuseModule.class, "@BindInterceptor annotation must be accompanied by @TransfuseModule");
        builder.given(BindInterceptors.class).requires(TransfuseModule.class, "@BindInterceptors annotation must be accompanied by @TransfuseModule");
        builder.given(BindProvider.class).requires(TransfuseModule.class, "@BindProvider annotation must be accompanied by @TransfuseModule");
        builder.given(BindProviders.class).requires(TransfuseModule.class, "@BindProviders annotation must be accompanied by @TransfuseModule");
        builder.given(DefineScope.class).requires(TransfuseModule.class, "@DefineScope annotation must be accompanied by @TransfuseModule");
        builder.given(DefineScopes.class).requires(TransfuseModule.class, "@DefineScopes annotation must be accompanied by @TransfuseModule");
        builder.given(UsesPermission.class).requires(TransfuseModule.class, "@UsesPermission annotation must be accompanied by @TransfuseModule");
        builder.given(UsesSdk.class).requires(TransfuseModule.class, "@UsesSdk annotation must be accompanied by @TransfuseModule");

        builder.given(Provides.class).requires(TransfuseModule.class, "@Provides annotation must be accompanied by @TransfuseModule");

        annotationValidator = builder.build();
    }

    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {

        for (ASTAnnotation annotation : astType.getAnnotations()) {
            validateAnnotation(annotation, astType, astType.getAnnotations());
        }

        for (ASTConstructor astConstructor : astType.getConstructors()) {
            for (ASTAnnotation annotation : astConstructor.getAnnotations()) {
                validateAnnotation(annotation, astConstructor, astConstructor.getAnnotations(), astType.getAnnotations());

                for (ASTParameter astParameter : astConstructor.getParameters()) {
                    validateParameter(astParameter, astConstructor, astType);
                }
            }
        }
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTType concreteType, ASTMethod astMethod, AnalysisContext context) {
        for (ASTAnnotation annotation : astMethod.getAnnotations()) {
            validateAnnotation(annotation, astMethod, astMethod.getAnnotations(), concreteType.getAnnotations());

            for (ASTParameter astParameter : astMethod.getParameters()) {
                validateParameter(astParameter, astMethod, concreteType);
            }
        }
    }

    @Override
    public void analyzeField(InjectionNode injectionNode, ASTType concreteType, ASTField astField, AnalysisContext context) {
        for (ASTAnnotation annotation : astField.getAnnotations()) {
            validateAnnotation(annotation, astField, astField.getAnnotations(), concreteType.getAnnotations());
        }
    }

    private void validateParameter(ASTParameter parameter, ASTBase containingAST, ASTType containingType){
        for (ASTAnnotation paramAnnotation : parameter.getAnnotations()) {
            validateAnnotation(paramAnnotation, parameter, parameter.getAnnotations(), containingAST.getAnnotations(), containingType.getAnnotations());
        }
    }

    private void validateAnnotation(ASTAnnotation annotation, ASTBase astBase, ImmutableSet<ASTAnnotation>... applicableAnnotations){
        annotationValidator.validate(annotation, astBase, flatten(applicableAnnotations));
    }

    private ImmutableSet<ASTAnnotation> flatten(ImmutableSet<ASTAnnotation>... sets){
        ImmutableSet.Builder<ASTAnnotation> combineBuilder = ImmutableSet.builder();
        for (ImmutableSet<ASTAnnotation> set : sets) {
            combineBuilder.addAll(set);
        }
        return combineBuilder.build();
    }
}
