package com.voisen.rxactivity.anno;

import com.voisen.rxactivity.utils.RxNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RxFragment {
    String path() default "";
    Class<?> clazz() default RxNull.class;
    String className() default "";
}
