package com.courier_tracking.aspect.annotation;

import com.courier_tracking.controller.request.CourierRequest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogOperation {
    String lat() default "";
    String lon() default "";
    String courierId() default "";
    String timeStamp() default "";
}