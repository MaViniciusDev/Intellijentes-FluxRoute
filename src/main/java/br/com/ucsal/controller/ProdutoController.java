package br.com.ucsal.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.ucsal.controller.operations.Command;
import br.com.ucsal.controller.operations.Rota;
import br.com.ucsal.controller.managers.InjectManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/view/*")  // Mapeia todas as requisições com "/view/*"
public class ProdutoController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        System.out.println(path);
        Map<String,Command>commands = (Map<String, Command>) request.getServletContext().getAttribute("command");
        Command command = commands.get(path);

        if (command != null) {
            command.execute(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Página não encontrada");
        }
    }
}
