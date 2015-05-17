/**
 * Copyright 2011-2015 John Ericksen
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
package org.androidtransfuse.inject;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LibraryDependency1 {

    private final LibraryDependency2 libraryDependency2;
    private final LibraryDependency3 libraryDependency3;
    private final LibraryDependency libraryDependency;

    @Inject
    public LibraryDependency1(LibraryDependency2 libraryDependency2, LibraryDependency3 libraryDependency3, LibraryDependency libraryDependency){
        this.libraryDependency2 = libraryDependency2;
        this.libraryDependency3 = libraryDependency3;
        this.libraryDependency = libraryDependency;
    }

    public LibraryDependency2 getLibraryDependency2() {
        return libraryDependency2;
    }

    public LibraryDependency3 getLibraryDependency3() {
        return libraryDependency3;
    }

    public LibraryDependency getLibraryDependency() {
        return libraryDependency;
    }
}
