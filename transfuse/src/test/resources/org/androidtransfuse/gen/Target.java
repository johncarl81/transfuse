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
package org.androidtransfuse.gen;

/**
 * @author John Ericksen
 */
public class Target extends Subclass {

    public String one;
    protected String two;
    String three;
    private String four;

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    protected String getTwo() {
        return two;
    }

    protected void setTwo(String two) {
        this.two = two;
    }

    String getThree() {
        return three;
    }

    void setThree(String three) {
        this.three = three;
    }

    private String getFour() {
        return four;
    }

    private void setFour(String four) {
        this.four = four;
    }

    public String getPrivateFour() {
        return four;
    }
}
