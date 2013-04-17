package org.richfaces.cdk.templatecompiler.statements;

import java.util.Set;

import javax.xml.namespace.QName;

import org.richfaces.cdk.attributes.Attribute.Kind;

import com.google.common.collect.Sets;

/**
 * <p class="changed_added_4_0">
 * object contains information about attribute
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public final class PassThrough implements Comparable<PassThrough> {
    QName name;
    Kind kind = Kind.GENERIC;
    String componentAttribute;
    String type;
    Object defaultValue;
    Set<String> behaviors = Sets.newHashSet();

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the name
     */
    public QName getName() {
        return this.name;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the kind
     */
    public Kind getKind() {
        return this.kind;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the componentAttribute
     */
    public String getComponentAttribute() {
        return this.componentAttribute;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the type
     */
    public String getType() {
        return this.type;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the defaultValue
     */
    public Object getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the behaviors
     */
    public Set<String> getBehaviors() {
        return this.behaviors;
    }

    public String getBuilderMethod() {
        if (Kind.BOOLEAN.equals(kind)) {
            return "bool";
        } else {
            return kind.toString().toLowerCase();
        }
    }

    @Override
    public int compareTo(PassThrough o) {
        return name.toString().compareTo(o.name.toString());
    }
}