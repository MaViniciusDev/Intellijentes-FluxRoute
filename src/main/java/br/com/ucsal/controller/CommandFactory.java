package br.com.ucsal.controller;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("create", new ProdutoAdicionarServlet());
        commands.put("read", new ProdutoListarServlet());
        commands.put("update", new ProdutoEditarServlet());
        commands.put("delete", new ProdutoExcluirServlet());

        // Adicione outros comandos aqui
    }

    public static Command getCommand(String action) {
        return commands.getOrDefault(action, request -> {
            throw new IllegalArgumentException("Comando inválido: " + action);
        });
    }
}
