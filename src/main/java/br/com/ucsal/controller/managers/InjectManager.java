package br.com.ucsal.controller.managers;

import br.com.ucsal.controller.operations.Inject;
import br.com.ucsal.controller.operations.ImplementationOf;
import br.com.ucsal.controller.operations.Singleton;

import java.lang.reflect.Field;

public class InjectManager {
    public static void resolveDependencies(Object instance) {
        try {
            for (var field : instance.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    var dependency = createDependencyForField(field);
                    if (dependency != null) {
                        field.setAccessible(true);
                        field.set(instance, dependency);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object createDependencyForField(Field field) throws Exception {
        var fieldType = field.getType();
        Class<?> dependencyType = fieldType;

        if (fieldType.isInterface()) {
            var implementationAnnotation = field.getAnnotation(ImplementationOf.class);
            if (implementationAnnotation != null) {
                dependencyType = implementationAnnotation.value();
            } else {
                throw new RuntimeException("Cannot resolve dependency for " + fieldType.getName() + " without @ImplementationOf annotation");
            }
        }

        return instantiateDependency(dependencyType);
    }

    private static Object instantiateDependency(Class<?> dependencyType) throws Exception {
        if (dependencyType.isAnnotationPresent(Singleton.class)) {
            return SingletonManager.getInstance(dependencyType);
        }
        Object dependency = dependencyType.getDeclaredConstructor().newInstance();
        resolveDependencies(dependency); // Recursive injection for nested dependencies
        return dependency;
    }


}