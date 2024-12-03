package br.com.ucsal.controller.managers;

import br.com.ucsal.controller.operations.Singleton;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonManager {

    // Mapa thread-safe para armazenar as instâncias únicas (singletons)
    private static final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();

    public static <T> T getInstance(Class<T> clazz) {
        // Verifica se a classe está anotada com @Singleton
        if (!clazz.isAnnotationPresent(Singleton.class)) {
            throw new IllegalArgumentException(
                    "A classe " + clazz.getName() + " não está anotada com @Singleton."
            );
        }
        // Usa computeIfAbsent para garantir o thread-safe
        return (T) instances.computeIfAbsent(clazz, SingletonManager::createInstance);
    }

    private static <T> T createInstance(Class<T> clazz) {
        try {
            // Acessa o construtor sem argumentos
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true); // Permite acesso ao construtor privado
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao criar uma instância da classe: " + clazz.getName(), e
            );
        }
    }
}
