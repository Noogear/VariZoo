package cn.variZoo.Configuration.Form;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for adding single line comments to configuration fields.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Comments {
    String[] value() default {};

    String[] cn_value() default {};
}
