package com.voisen.rxactivity.anno;

import androidx.fragment.app.FragmentActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface TrickActivity {
    Class<? extends FragmentActivity> clazz() default FragmentActivity.class;
    String className() default "";
    int enterAnim() default -1;
    int exitAnim() default -1;
}
