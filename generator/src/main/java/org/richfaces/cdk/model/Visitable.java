package org.richfaces.cdk.model;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public interface Visitable {
    <R, D> R accept(Visitor<R, D> visitor, D data);
}
