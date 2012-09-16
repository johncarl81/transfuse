package org.androidtransfuse.analysis.repository;


import org.androidtransfuse.analysis.astAnalyzer.ASTAnalysis;

import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class AnalysisRepository {

    private final Set<ASTAnalysis> analysisSet = new HashSet<ASTAnalysis>();

    public void addAnalysis(ASTAnalysis astAnalysis) {
        analysisSet.add(astAnalysis);
    }

    public Set<ASTAnalysis> getAnalysisSet() {
        return analysisSet;
    }
}
