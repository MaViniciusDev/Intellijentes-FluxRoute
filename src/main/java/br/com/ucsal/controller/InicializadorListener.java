package br.com.ucsal.controller;

import br.com.ucsal.controller.operations.Command;
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

@WebListener
public class InicializadorListener implements ServletContextListener {

    private Map<String, Command> commands = new HashMap<>();


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources("");

            // Processa todos os diretórios encontrados nas resources
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());

                if (directory.exists() && directory.isDirectory()) {
                    runOperations(directory, context); // Processa os arquivos de classes
                }
            }

            // Registra os comandos no ServletContext
            context.setAttribute("commands", commands);
            System.out.println("Mapa de comandos registrado com sucesso!");

        } catch (Exception e) {
            // Tratamento melhorado de exceções
            System.err.println("Erro ao inicializar os recursos: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Inicializando recursos na inicialização da aplicação");
    }


    private void runOperations(File directory, ServletContext context) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    runOperations(file, context); // Recursivamente processa subdiretórios
                } else if (file.getName().endsWith(".class")) {
                    try {
                        String classVariable = file.getPath()
                                .replace(File.separator, ".")
                                .replaceFirst(".*?classes.", "") // Ajuste conforme sua estrutura
                                .replace(".class", "");

                        System.out.println("Processando classe: " + classVariable);

                        // Carrega a classe usando o ClassLoader
                        Class<?> clazz = Class.forName(classVariable, false, context.getClassLoader());

                        // Ignora interfaces e classes abstratas
                        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                            continue;
                        }

                        // Processa a classe anotada com @Singleton
                        if (clazz.isAnnotationPresent(Singleton.class)) {
                            SingletonManager.getInstance(clazz);
                            System.out.println("Classe anotada com @Singleton inicializada: " + classVariable);
                        }

                        // Processa a classe anotada com @Rota
                        if (clazz.isAnnotationPresent(Rota.class)) {
                            Rota rota = clazz.getAnnotation(Rota.class);

                            try {
                                Command command = (Command) clazz.getDeclaredConstructor().newInstance();
                                commands.put(rota.path(), command);

                                // Injeção de dependências nas rotas
                                InjectManager.resolveDependencies(command);
                                System.out.println("Rota registrada: " + rota.path());
                            } catch (Exception e) {
                                System.err.println("Erro ao instanciar ou registrar comando para a rota " + rota.path());
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        System.err.println("Erro ao inicializar os recursos: " + e.getMessage());
                    }
                }
            }
        } else {
            // Diretório inválido ou vazio
            System.err.println("Diretório inválido ou vazio: " + directory.getAbsolutePath());
        }
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Aplicação finalizada.");
    }
}
