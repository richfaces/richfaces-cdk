<#include "_copyright.ftl">

package ${sourceInterface.package};

import javax.annotation.Generated;
import ${listenerInterface};

/**
 * ${description?if_exists}
 **/
@Generated({"RichFaces CDK", "4.0.0-SNAPSHOT"})
public interface ${sourceInterface.simpleName} {

    /**
     * <p>Add a new {@link ${listenerInterface.simpleName}} to the set of listeners interested
     * in being notified when {@link ${type}}s occur.</p>
     *
     * @param listener The {@link ${listenerInterface.simpleName}} to be added
     *
     * @throws NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void add${listenerInterface.simpleName}(${listenerInterface.simpleName} listener);

    /**
     * <p>Remove an existing {@link ${listenerInterface.simpleName}} (if any) from the set of
     * listeners interested in being notified when {@link ${type}}s
     * occur.</p>
     *
     * @param listener The {@link ${listenerInterface.simpleName}} to be removed
     *
     * @throws NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void remove${listenerInterface.simpleName}(${listenerInterface.simpleName} listener);

    /**
     * <p>Return the array of registered {@link ${listenerInterface.simpleName}}s for this instance.  If there are no registered listeners,
     * a zero-length array is returned.</p>
     */
    public ${listenerInterface.simpleName}[] get${listenerInterface.simpleName}s();
    
}