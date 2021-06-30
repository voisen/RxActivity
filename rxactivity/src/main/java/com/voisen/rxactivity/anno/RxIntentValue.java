package com.voisen.rxactivity.anno;

import com.voisen.rxactivity.RxIntentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Inherited
public @interface RxIntentValue {
    /**
     * type
     */
    RxIntentType type() default RxIntentType.None;
    RxIntentType value() default RxIntentType.None;

    /**
     * Ignore null parameters, default true
     */
    boolean ignoreNull() default true;
}
