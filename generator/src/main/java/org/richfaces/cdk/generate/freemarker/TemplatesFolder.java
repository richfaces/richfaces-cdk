package org.richfaces.cdk.generate.freemarker;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

@Retention(RUNTIME)
@Target({ FIELD, PARAMETER, METHOD })
@BindingAnnotation
public @interface TemplatesFolder {

}
