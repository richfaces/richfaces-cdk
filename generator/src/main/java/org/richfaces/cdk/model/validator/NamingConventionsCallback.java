package org.richfaces.cdk.model.validator;

import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.FacesId;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public interface NamingConventionsCallback {
    /**
     * <p class="changed_added_4_0">
     * Infer JSF type by Java class name
     * </p>
     *
     * @param targetClass
     * @return
     */
    FacesId inferType(ClassName targetClass) throws CallbackException;

    /**
     * <p class="changed_added_4_0">
     * Last resort - infer default JSF type
     * </p>
     *
     * @return
     */
    FacesId inferType() throws CallbackException;

    /**
     * <p class="changed_added_4_0">
     * Infer Java class name by JSF type
     * </p>
     *
     * @param id
     * @return
     */
    ClassName inferClass(FacesId id) throws CallbackException;

    /**
     * <p class="changed_added_4_0">
     * Infer default base class for cenerated component.
     * </p>
     *
     * @return
     * @throws CallbackException
     */
    ClassName getDefaultBaseClass() throws CallbackException;

    /**
     * <p class="changed_added_4_0">
     * Infer default Java class name.
     * </p>
     *
     * @return
     * @throws CallbackException
     */
    ClassName getDefaultClass() throws CallbackException;
}