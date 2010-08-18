package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * This is a <a href="http://jcp.org/en/jsr/detail?id=305">JSR-305</a> annotation. The presence of this annotation
 * indicates that parameter or field may have null value
 * @author asmirnov
 * @version $Id$
 *
 */
@Documented

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface Nullable {

}
