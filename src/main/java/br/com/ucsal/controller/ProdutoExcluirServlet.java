package br.com.ucsal.controller;

import java.io.IOException;

import br.com.ucsal.controller.operations.Command;
import br.com.ucsal.controller.operations.Inject;
import br.com.ucsal.controller.operations.Rota;
import br.com.ucsal.persistencia.HSQLProdutoRepository;
import br.com.ucsal.service.ProdutoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Rota(path = "/excluirProduto")
public class ProdutoExcluirServlet implements Command {
	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoService produtoService;

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Lógica de exclusão
		Integer id = Integer.parseInt(request.getParameter("id"));
		produtoService.removerProduto(id);
		response.sendRedirect("listarProdutos");
	}

}
