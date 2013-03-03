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
package org.androidtransfuse.config;

import java.io.File;
import java.net.URI;

/**
 * @author John Ericksen
 */
public class FileProxy extends File {

    private static final long serialVersionUID = -637507751011590863L;

    public FileProxy(String pathname) {
        super(pathname);
    }

    public FileProxy(String parent, String child) {
        super(parent, child);
    }

    public FileProxy(File parent, String child) {
        super(parent, child);
    }

    public FileProxy(URI uri) {
        super(uri);
    }
}
