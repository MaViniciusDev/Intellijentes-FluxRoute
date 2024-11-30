package br.com.ucsal.controller;

import br.com.ucsal.controller.operations.Command;
import br.com.ucsal.controller.operations.Inject;
import br.com.ucsal.controller.operations.Rota;
import br.com.ucsal.controller.operations.Singleton;
import br.com.ucsal.controller.managers.InjectManager;
import br.com.ucsal.controller.managers.SingletonManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class InicializadorListener implements ServletContextListener {

    private Map<String, Command> commands = new HashMap<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources("");

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());

                if (directory.exists() && directory.isDirectory()) {
                    processDirectory(directory, context);
                }
            }

            // Registra os comandos no ServletContext
            context.setAttribute("commands", commands);
            System.out.println("Mapa de comandos registrado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Inicializando recursos na inicialização da aplicação");
    }

    private void processDirectory(File directory, ServletContext context) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    processDirectory(file, context);
                } else if (file.getName().endsWith(".class")) {
                    try {
                        String classVariable = file.getPath()
                                .replace(File.separator, ".")
                                .replaceFirst(".*?classes.", "") // Ajuste conforme sua estrutura
                                .replace(".class", "");

                        System.out.println("Processando classe: " + classVariable);

                        Class<?> clazz = Class.forName(classVariable, false, context.getClassLoader());

                        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                            continue;
                        }

                        if (clazz.isAnnotationPresent(Singleton.class)) {
                            SingletonManager.getInstance(clazz);
                            System.out.println("Classe anotada com @Singleton inicializada: " + classVariable);
                        }

                        if (clazz.isAnnotationPresent(Rota.class)) {
                            Rota rota = clazz.getAnnotation(Rota.class);
                            Command command = (Command) clazz.getDeclaredConstructor().newInstance();
                            commands.put(rota.path(), command);
                            InjectManager.injectDependencies(command);
                            System.out.println("Rota registrada: " + rota.path());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Erro ao processar a classe: " + file.getName());
                    }
                }
            }
        } else {
            System.err.println("Diretório inválido ou vazio: " + directory.getAbsolutePath());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Aplicação finalizada.");
    }
}
