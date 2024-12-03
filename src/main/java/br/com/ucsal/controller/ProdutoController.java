package br.com.ucsal.controller;

import java.io.IOException;
import java.util.Map;

import br.com.ucsal.controller.operations.Command;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/view/*")
public class ProdutoController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        System.out.println("Path solicitado: " + path);

        // Recupera o mapa de comandos
        Object commandsObj = request.getServletContext().getAttribute("commands");

        if (commandsObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Command> commands = (Map<String, Command>) commandsObj;

            Command command = commands.get(path);

            if (command != null) {
                try {
                    command.execute(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar a requisição.");
                }
            } else {
                System.err.println("Comando não encontrado para o path: " + path);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Página não encontrada.");
            }
        } else {
            System.err.println("O atributo 'commands' no ServletContext não é um mapa válido.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro interno: comandos não configurados.");
        }
    }
}
