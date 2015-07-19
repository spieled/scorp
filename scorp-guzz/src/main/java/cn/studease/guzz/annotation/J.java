package cn.studease.guzz.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface J {
    String value();

    int index() default 5000;
}


