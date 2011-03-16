package org.richfaces.cdk.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.faces.convert.Converter;
import javax.faces.validator.Validator;

/**
 * This annotation defines concrete class as JSF {@link Validator}, or abstract class as the base for generated
 * Validator implementation.
 * 
 * @author akolonitsky
 * @since Jan 13, 2010
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface JsfValidator {

    public static final String NAME = "org.richfaces.cdk.annotations.JsfValidator";

    /**
     * <p class="changed_added_4_0">
     * The "validator-id" element represents the identifier under which the corresponding Validator class should be
     * registered.
     * </p>
     * 
     * @return
     */
    public String id();

    /**
     * <p class="changed_added_4_0">
     * fully qualified class name of the generated Converter implementation. Default value means nothing to genrate from concrete class,
     * or infer name by convention for abstract class.</p>
     * @return
     */
    public String generate() default "";

    /**
     * <p class="changed_added_4_0">
     * Tag description. If generated tags require special handlers, provide separate description for every type of tag, JSP and Facelets.
     * Otherwise, the only one tag tag description with name and type {@link TagType#All}. 
     * </p>
     * @return
     */
    public Tag[] tag() default {};

    /**
     * <p class="changed_added_4_0">
     * Description to include into generated faces-config and taglib.
     * </p>
     * 
     * @return
     */
    public Description description() default @Description();

    /**
     * <p class="changed_added_4_0">
     * Defines file names for fragment of faces-config.xml that contain standard attribute definitions. All names relative to the
     * META-INF/cdk/attributes/ folder in classpath. CDK also tries to read
     * META-INF/cdk/attributes/[classname].xml file for all component superclasses and interfaces. Therefore, it is not
     * necessary to explicitly include definitions for Converter and any other standard JSF classes.
     * </p>
     * 
     * @return
     */
    public String[] attributes() default {};

    /**
     * <p class="changed_added_4_0">
     * Interfaces that should be implemented in the generated class. CDK processes all {@link Attribute} annotations in
     * these interfaces
     * </p>
     * 
     * @return
     */
    public Class<?>[] interfaces() default {};

}
