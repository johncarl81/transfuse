package org.androidtransfuse.util;

/**
 * @author John Ericksen
 */
public class Contract {

    private Contract(){
        //private utility constructor
    }

    public static void notNull(Object value, String name){
        throw new IllegalArgumentException("Unexpected null with " + name);
    }
}
