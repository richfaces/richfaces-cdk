package org.richfaces.cdk.model;

import java.io.Serializable;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 * @param <T>
 */
public interface ModelElement<T extends ModelElement<? super T>> extends Serializable, Mergeable<T>, Visitable {
}
