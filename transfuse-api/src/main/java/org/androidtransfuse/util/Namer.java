package org.androidtransfuse.util;

/**
 * @author John Ericksen
 */
public final class Namer {

    private static final String SEPARATOR = "$$";

    private StringBuilder builder = new StringBuilder();

    private Namer(String name){
        //private utility class constructor
        builder.append(name);
    }

    public static Namer name(String name){
        return new Namer(name);
    }

    public Namer append(String part) {
        builder.append(SEPARATOR);
        builder.append(part);
        return this;
    }

    public Namer append(int part) {
        builder.append(SEPARATOR);
        builder.append(part);
        return this;
    }

    public String build(){
        return builder.toString();
    }

    public String toString(){
        return build();
    }
}

