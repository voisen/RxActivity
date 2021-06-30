package com.voisen.rxactivity.anno;

import com.voisen.rxactivity.utils.RxNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface RxActivity {
    Class<?> clazz() default RxNull.class;
    String path() default "";
    String className() default "";
    int enterAnim() default -1;
    int exitAnim() default -1;
}
