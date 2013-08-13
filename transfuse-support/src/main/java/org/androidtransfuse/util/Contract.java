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
package org.androidtransfuse.util;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;

/**
 * @author John Ericksen
 */
public final class Contract {

    private Contract(){
        //private utility constructor
    }

    /**
     * Throw a null pointer exception if object is null
     * @param object the object to test
     * @param objectName the name of the object
     * @throws IllegalArgumentException if the passed object is null
     */
    public static void notNull(final Object object, final String objectName) {
        if (object == null) {
            throw new IllegalArgumentException("expecting non-null value for " + maskNullArgument(objectName));
        }
    }

    /**
     * Checks that the collections have the same number of elements, otherwise throws an exception
     * @param collection1 the first collection
     * @param collection2 the second collection
     * @param collection1Name the name of the first collection
     * @param collection2Name the name of the second collection
     * @throws IllegalArgumentException if collection1 or collection2 are null or if the collections don't agree on the number of elements
     */
    public static void sameSize(final Collection<?> collection1, final Collection<?> collection2, final String collection1Name, final String collection2Name) {
        notNull(collection1, collection1Name);
        notNull(collection2, collection2Name);

        if (collection1.size() != collection2.size()) {
            throw new IllegalArgumentException("expecting " + maskNullArgument(collection1Name) + " to have the same size as " + maskNullArgument(collection2Name));
        }
    }

    /**
     * Checks that a collection is of a given size
     * @param collection the collection to check
     * @param size the size of the collection
     * @param collectionName the name of the collection
     * @throws IllegalArgumentException if collection is null or if the collection size is not as expected
     */
    public static void atSize(final Collection<?> collection, final int size, final String collectionName) {
        notNull(collection, collectionName);
        notNegative(size, "size");

        if (collection.size() != size) {
            throw new IllegalArgumentException("expecting " + maskNullArgument(collectionName) + " to be of size " + size + ".");
        }
    }

    /**
     * Check that a collection is not empty
     * @param collection the collection to check
     * @param collectionName the name of the collection
     * @throws IllegalArgumentException if collection is null or if the collection is empty
     */
    public static void notEmpty(final Collection<?> collection, final String collectionName) {
        notNull(collection, collectionName);

        if (collection.isEmpty()) {
            throw new IllegalArgumentException("expecting " + maskNullArgument(collectionName) + " to contain 1 or more elements");
        }
    }

    /**
     * Check that an array is not empty
     * @param array the array to check
     * @param arrayName the name of the array
     * @throws IllegalArgumentException if array is null or if the array is empty
     */
    public static void notEmpty(final Object[] array, final String arrayName) {
        notNull(array, arrayName);

        if (array.length == 0) {
            throw new IllegalArgumentException("expecting " + maskNullArgument(arrayName) + " to contain 1 or more elements");
        }
    }

    /**
     * Checks that the arrays have the same number of elements, otherwise throws and exception
     * @param array1 the first array
     * @param array2 the second array
     * @param array1Name the name of the first array
     * @param array2Name the name of the second array
     * @throws IllegalArgumentException if array1 or array2 are null or if the arrays don't agree on the number of elements
     */
    public static void sameLength(final Object[] array1, final Object[] array2, final String array1Name, final String array2Name) {
        notNull(array1, array1Name);
        notNull(array2, array2Name);

        if (array1.length != array2.length) {
            throw new IllegalArgumentException("expecting " + maskNullArgument(array1Name) + " to have the same length as " + maskNullArgument(array2Name));
        }
    }

    /**
     * Checks that an array is of a given length
     * @param array the array
     * @param length the desired length of the array
     * @param arrayName the name of the array
     * @throws IllegalArgumentException if the array is null or if the array's length is not as expected
     */
    public static void atLength(final Object[] array, final int length, final String arrayName) {
        notNull(array, arrayName);
        notNegative(length, "length");

        if (array.length != length) {
            throw new IllegalArgumentException("expecting " + maskNullArgument(arrayName) + " to be of length " + length + ".");
        }
    }

    /**
     * Checks that the input value is within the bounds of a maximum or minimum value
     * @param input the input to check
     * @param min the minimum value of the input (if null, input is not bound by minimum value)
     * @param max the maximum value of the input (if null, input is not bound by maximum value)
     * @param inputName the name of the input to report in error
     * @throws IllegalArgumentException if input is null or if the input is less than min or greater than max
     */
    public static void inBounds(final Integer input, final Integer min, final Integer max, final String inputName) {
        notNull(input, inputName);

        if ((min != null) && (input < min)) {
            throw new IndexOutOfBoundsException("a value of " + input.toString() + " was unexpected for " + maskNullArgument(inputName) + ", it is expected to be less than " + min.toString());
        }

        if ((max != null) && (input > max)) {
            throw new IndexOutOfBoundsException("a value of " + input.toString() + " was unexpected for " + maskNullArgument(inputName) + ", it is expected to be greater than " + max.toString());
        }
    }

    /**
     * Checks that the input value is non-negative
     * @param input the input to check
     * @param inputName the name of the input
     * @throws IllegalArgumentException if input is null or if the input is less than zero
     */
    public static void notNegative(final Integer input, final String inputName) {
        notNull(input, inputName);

        if (input < 0) {
            throw new IllegalArgumentException("a value of " + input.toString() + " was unexpected for " + maskNullArgument(inputName) + ", it is expected to be positive");
        }
    }

    /**
     * Checks that the input value is non-zero
     * @param input the input to check
     * @param inputName the name of the input
     * @throws IllegalArgumentException if input is null or if the input is zero
     */
    public static void notZero(final Integer input, final String inputName) {
        notNull(input, inputName);

        if (input == 0) {
            throw new IllegalArgumentException("a zero value for was unexpected for " + maskNullArgument(inputName) + ", it is expected to be non zero");
        }
    }

    /**
     * Checks that an input string is non blank
     * @param input the input
     * @param inputName the name of the input
     * @throws IllegalArgumentException if input is null or if the input is blank
     */
    public static void notBlank(final String input, final String inputName) {
        notNull(input, inputName);

        if (StringUtils.isBlank(input)) {
            throw new IllegalArgumentException("Expecting " + maskNullArgument(inputName) + " to be a non blank value.");
        }
    }

    /**
     * Check that an input of a given class
     * @param input the input
     * @param classType the class to check for
     * @param inputName the name of the input
     */
    public static void instanceOf(final Object input, Class<?> classType, final String inputName) {
        notNull(input, "input");
        notNull(classType, "classType");

        if (!classType.isInstance(input)) {
            throw new IllegalArgumentException("Expecting " + maskNullArgument(inputName) + " to an instance of " + classType.getName() + ".");
        }
    }

    /**
     * Return the input if not null, otherwise return the string '-unknown argument-'
     * @param input the input string to deal with
     * @return the input if not null, '-unknown argument-' otherwise
     */
    private static String maskNullArgument(final String input) {
        return (input == null) ? "-unknown argument-" : input;
    }
}
