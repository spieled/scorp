package cn.studease.annotation;

/**
 * Author: liushaoping
 * Date: 2015/8/11.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface J {
    String value();

    int index() default 5000;
}