package br.com.ucsal.persistencia;

import br.com.ucsal.controller.managers.SingletonManager;

public class PersistenciaFactory {


	public static final int MEMORIA = 0;
	public static final int HSQL = 1;


	public static ProdutoRepository<?, ?> getProdutoRepository(int type) {
		ProdutoRepository<?, ?> produtoRepository;

		switch (type) {
		case 0: {
			produtoRepository = SingletonManager.getInstance(MemoriaProdutoRepository.class);
			break;
		}
		case 1: {
			produtoRepository = SingletonManager.getInstance(HSQLProdutoRepository.class);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
		return produtoRepository;
	}
}
