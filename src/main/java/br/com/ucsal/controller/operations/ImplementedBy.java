package br.com.ucsal.controller.operations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // Applies to classes or interfaces
@Retention(RetentionPolicy.RUNTIME) // Available at runtime
public @interface ImplementedBy {
    Class<?> value(); // Specifies the implementation class
}
