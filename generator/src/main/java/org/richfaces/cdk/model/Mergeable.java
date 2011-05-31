package org.richfaces.cdk.model;

public interface Mergeable<T> {
    /**
     * <p class="changed_added_4_0">
     * Merge values from other object.
     * </p>
     *
     * @param other
     */
    void merge(T other);

    /**
     * <p class="changed_added_4_0">
     * Compare object with other. For example, JSF component with same type or class is the same component.
     * </p>
     *
     * @param other
     * @return
     */
    boolean same(T other);
}
