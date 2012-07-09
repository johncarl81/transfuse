package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

/**
 * Analysis interface targeting the individual elements of an ASTType.
 *
 * @author John Ericksen
 */
public interface ASTAnalysis {

    /**
     * Analyze the given type.  Used during the class scanning phase to perform class level operations.
     *
     * @param injectionNode current injection node
     * @param astType       type
     * @param context       current context
     */
    void analyzeType(InjectionNode injectionNode, ASTType astType, AnalysisContext context);

    /**
     * Analyze the given method.  Used during the class scanning phase to perform method level operations.
     *
     * @param injectionNode current injection node
     * @param concreteType
     * @param astMethod     method
     * @param context       current context
     */
    void analyzeMethod(InjectionNode injectionNode, ASTType concreteType, ASTMethod astMethod, AnalysisContext context);

    /**
     * Analyze the given field.  Used during the class scanning phase to perform field level operations.
     *
     * @param injectionNode current injection node
     * @param concreteType
     * @param astField      field
     * @param context       current context
     */
    void analyzeField(InjectionNode injectionNode, ASTType concreteType, ASTField astField, AnalysisContext context);
}
