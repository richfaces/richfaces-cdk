/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.cdk.util;

/**
 * Helper methods for comparable types
 *
 * @author Nick Belaevski
 */
public final class ComparatorUtils {
    private ComparatorUtils() {
        // private constructor
    }

    /**
     * <p>
     * Does null-safe comparison of mutually comparable objects.
     * </p>
     *
     * <p>
     * Obeys common rules of {@link Comparable#compareTo(Object)} methods if both objects to compare are non-null.
     * </p>
     *
     * <p>
     * If one of the objects is <code>null</code>, but the other is not, then <code>null</code> object is considered to be less
     * than non-null.
     * </p>
     *
     * <p>
     * Two <code>null</code> objects are considered as equal.
     * </p>
     *
     * @param <T> the type of objects that this object may be compared to
     * @param t1 the first object
     * @param t2 the second object
     * @return
     */
    public static <T extends Comparable<T>> int nullSafeCompare(T t1, T t2) {
        if (t1 == null) {
            return t2 == null ? 0 : -1;
        } else if (t2 == null) {
            return 1;
        } else {
            return t1.compareTo(t2);
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Null-safe equals check. Return true if both arguments are null or equal.
     * </p>
     *
     * @param <T> the type of objects to compare.
     * @param t1
     * @param t2
     * @return true if both arguments are null or equal
     */
    public static <T> boolean nullSafeEquals(T t1, T t2) {
        if (t1 == null) {
            return t2 == null;
        } else if (t2 == null) {
            return false;
        } else {
            return t1.equals(t2);
        }
    }
}
