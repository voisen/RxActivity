package com.voisen.rxactivity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解式设置通用IntentValue值, 其优先级小于 @IntentValue 参数注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface RxIntentOptions {
    String action() default "";
    int flags() default -1;
    String packageName() default "";
    String type() default "";
    String identifier() default "";
}
