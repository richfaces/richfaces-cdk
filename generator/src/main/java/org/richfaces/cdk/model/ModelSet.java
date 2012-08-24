/*
 * $Id$
 *
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.cdk.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

/**
 * <p class="changed_added_4_0">
 * Base class for all model collections. This collection use {@link Mergeable#same(Object)} method instead of equals to lookup
 * objects in the {@link #contains(Object)} and {@link #remove(Object)} methods. In addition, it merges same objects instead of
 * replace in the {@link #add(ModelElement)} method.
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class ModelSet<E extends ModelElement<? super E>> implements ModelCollection<E>, Set<E>, Serializable {
    private static final long serialVersionUID = -1L;

    private final List<E> elements = Lists.newArrayList();
    private Comparator<E> comparator;

    public static <T extends ModelElement<? super T>> ModelSet<T> create() {
        return new ModelSet<T>();
    }

    public static <T extends ModelElement<? super T>> ModelSet<T> create(Comparator<T> comparator) {
        ModelSet<T> collection = new ModelSet<T>();
        collection.setComparator(comparator);
        return collection;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the comparator
     */
    public Comparator<E> getComparator() {
        return this.comparator;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param comparator the comparator to set
     */
    public void setComparator(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public E find(final Predicate<? super E> predicate) {
        try {
            return Iterables.find(elements, predicate);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public boolean add(E e) {
        if (null == e) {
            throw new NullPointerException();
        }
        for (E element : elements) {
            if (element.same(e)) {
                element.merge(e);
                return false;
            }
        }
        return elements.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;

        for (E e : c) {
            changed |= add(e);
        }

        return changed;
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        if (null != o) {
            Class<? extends Object> clazz = o.getClass();
            for (E element : elements) {
                if (clazz.isInstance(element) && element.same((E) o)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return elements.containsAll(c);
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        if (null == comparator) {
            return elements.iterator();
        } else {
            List<E> list = Lists.newArrayList(elements);
            Collections.sort(list, comparator);
            return Iterators.unmodifiableIterator(list.iterator());
        }
    }

    @Override
    public boolean remove(Object o) {
        if (null != o) {
            Class<? extends Object> clazz = o.getClass();
            for (E element : elements) {
                if (clazz.isInstance(element) && element.same((E) o)) {
                    elements.remove(element);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;

        for (Object e : c) {
            changed |= remove(e);
        }

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return elements.retainAll(c);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Object[] toArray() {
        Object[] array = elements.toArray();
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return elements.toArray(a);
    }

    @Override
    public <R, D> R accept(R result, Visitor<R, D> visitor, D data) {
        return null == result ? accept(visitor, data) : result;
    }

    public <R, D> R accept(Visitor<R, D> visitor, D data) {
        for (ModelElement element : elements) {
            R result = element.accept(visitor, data);
            if (null != result) {
                return result;
            }
        }
        return null;
    }
}
