package org.androidtransfuse.scope;

/**
 * @author John Ericksen
 */
public class ScopeKey<T> {

    private Class<T> type;
    private String qualifier;

    public ScopeKey(Class<T> type) {
        this(type, null);
    }

    public ScopeKey(Class<T> type, String qualifier) {
        this.type = type;
        this.qualifier = qualifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof ScopeKey)){
            return false;
        }

        ScopeKey scopeKey = (ScopeKey) o;

        return !(qualifier != null ? !qualifier.equals(scopeKey.qualifier) : scopeKey.qualifier != null) && type.equals(scopeKey.type);

    }

    @Override
    public int hashCode() {
        return 31 * type.hashCode() + (qualifier != null ? qualifier.hashCode() : 0);
    }
}
