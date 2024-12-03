package br.com.ucsal.persistencia;

import br.com.ucsal.controller.managers.SingletonManager;

public class PersistenciaFactory {


	public static final int MEMORIA = 0;
	public static final int HSQL = 1;


	public static ProdutoRepository<?, ?> getProdutoRepository(int type) {
		ProdutoRepository<?, ?> produtoRepository;

		switch (type) {
			case MEMORIA: {
			produtoRepository = SingletonManager.getInstance(MemoriaProdutoRepository.class);
			break;
		}
			case HSQL: {
			produtoRepository = new HSQLProdutoRepository();
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
		return produtoRepository;
	}
}
