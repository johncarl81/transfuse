package org.androidtransfuse.matcher;

import org.androidtransfuse.analysis.adapter.ASTType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class Matcher {

    private Map<Match, MatchExecution> matches = new HashMap<Match, MatchExecution>();
    private List<Match> orderedMatch = new ArrayList<Match>();

    public void match(ASTType type) {
        for (Match match : orderedMatch) {
            if (match.matches(type)) {
                matches.get(match).execute(type);
                return;
            }
        }
    }

    public void addMatcher(Match match, MatchExecution matchExecution) {
        matches.put(match, matchExecution);
        orderedMatch.add(match);
    }
}
