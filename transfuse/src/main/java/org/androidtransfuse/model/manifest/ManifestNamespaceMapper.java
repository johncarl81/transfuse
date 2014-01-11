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
package org.androidtransfuse.model.manifest;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class ManifestNamespaceMapper extends NamespacePrefixMapper {

    public static final String DEFAULT_PREFIX = "";
    public static final String DEFAULT_URI = "default";

    public static final String TRANSFUSE_PREFIX = "t";
    public static final String TRANSFUSE_URI = "http://androidtransfuse.org";

    public static final String ANDROID_PREFIX = "android";
    public static final String ANDROID_URI = "http://schemas.android.com/apk/res/android";
 
    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if(TRANSFUSE_URI.equals(namespaceUri)) {
            return TRANSFUSE_PREFIX;
        } else if(ANDROID_URI.equals(namespaceUri)) {
            return ANDROID_PREFIX;
        } else if(DEFAULT_URI.equals(namespaceUri)) {
            return DEFAULT_PREFIX;
        }
        return suggestion;
    }
 
    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[] {TRANSFUSE_URI, ANDROID_URI, DEFAULT_PREFIX };
    }
 
}