package br.com.ucsal.controller.operations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // Type é usado pra classes
@Retention(RetentionPolicy.RUNTIME)
public @interface Singleton {
}
