package org.richfaces.cdk.model;

import org.richfaces.cdk.util.ComparatorUtils;

import com.google.common.base.Predicate;

public interface FacesComponent {
    /**
     * <p class="changed_added_4_0">
     * This predicate can be used to lookup component in {@link ModelCollection} by id.
     * </p>
     *
     * @author asmirnov@exadel.com
     *
     */
    public static final class ComponentPredicate implements Predicate<GeneratedFacesComponent> {
        private final FacesId id;

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param id
         */
        public ComponentPredicate(FacesId id) {
            this.id = id;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param name
         */
        public ComponentPredicate(String name) {
            this.id = FacesId.parseId(name);
        }

        @Override
        public boolean apply(GeneratedFacesComponent input) {
            return ComparatorUtils.nullSafeEquals(input.getId(), this.id);
        }
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return
     */
    FacesId getId();

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param id
     */
    void setId(FacesId id);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param targetClass the targetClass to set
     */
    void setTargetClass(ClassName targetClass);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the targetClass
     */
    ClassName getTargetClass();
}