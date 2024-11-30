package br.com.ucsal.controller.managers;

import br.com.ucsal.controller.operations.Inject;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InjectManager {

    private static final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();
    private static final ThreadLocal<Map<Class<?>, Boolean>> creationStack = ThreadLocal.withInitial(ConcurrentHashMap::new);

    /**
     * Injeta as dependências nos campos anotados com @Inject de uma instância fornecida.
     */
    public static <T> void injectDependencies(T instance) {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true); // Permitir acesso a campos privados
                try {
                    Object dependency = getInstance(field.getType());
                    field.set(instance, dependency); // Define a dependência no campo
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao injetar dependência no campo: " + field.getName(), e);
                }
            }
        }
    }

    /**
     * Resolve a instância de uma classe gerenciada, criando-a se necessário.
     */

    public static <T> T resolve(Class<T> clazz) {
        return getInstance(clazz);
    }

    /**
     * Obtém ou cria uma instância de uma classe.
     */
    private static <T> T getInstance(Class<T> clazz) {
        return (T) instances.computeIfAbsent(clazz, InjectManager::createInstance);
    }

    /**
     * Cria uma nova instância de uma classe e injeta suas dependências.
     */

    private static <T> T createInstance(Class<T> clazz) {
        if (creationStack.get().getOrDefault(clazz, false)) {
            throw new RuntimeException("Dependência circular detectada para a classe:" + clazz.getName());
        }

        creationStack.get().put(clazz, true);
        try {
            T instance = clazz.getDeclaredConstructor().newInstance(); // Cria a instância
            injectDependencies(instance); // Injeta as dependências
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar a instância da classe: " + clazz.getName(), e);
        } finally {
            creationStack.get().remove(clazz);
        }
    }
}
