package org.androidtransfuse.inject;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LibraryDependency1 {

    private LibraryDependency2 libraryDependency2;

    @Inject
    public LibraryDependency1(LibraryDependency2 libraryDependency2){
        this.libraryDependency2 = libraryDependency2;
    }

    public LibraryDependency2 getLibraryDependency2() {
        return libraryDependency2;
    }
}
