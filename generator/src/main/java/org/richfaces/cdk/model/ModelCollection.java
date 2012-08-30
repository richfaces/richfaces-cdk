package org.richfaces.cdk.model;

import java.io.Serializable;
import java.util.Collection;

import com.google.common.base.Predicate;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 * @param <E>
 */
public interface ModelCollection<E extends ModelElement<? super E>> extends Collection<E>, Visitable, Serializable {
    E find(final Predicate<? super E> predicate);

    <R, D> R accept(R result, Visitor<R, D> visitor, D data);
}