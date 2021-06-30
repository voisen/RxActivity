package com.voisen.rxactivity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Automatically import the Intent extra parameters !
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface RxAutowired {
    String value() default "";
    String key() default "";
    boolean ignoreNull() default true;
}
