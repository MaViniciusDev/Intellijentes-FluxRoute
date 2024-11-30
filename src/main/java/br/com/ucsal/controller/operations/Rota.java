package br.com.ucsal.controller.operations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Rota {
    String path(); // The route path
    String method() default "GET"; // The HTTP method (GET, POST, etc.)
}