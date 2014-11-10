package org.androidtransfuse.util.matcher;

import com.google.common.base.Predicate;

/**
 * @author John Ericksen
 */
public class MatcherPredicate<T> implements Predicate<T> {

    private Matcher<T> matcher;

    private MatcherPredicate(Matcher<T> matcher) {
        this.matcher = matcher;
    }

    public static <T> MatcherPredicate<T> build(Matcher<T> matcher){
        return new MatcherPredicate<T>(matcher);
    }

    @Override
    public boolean apply(T input) {
        return matcher.matches(input);
    }
}
