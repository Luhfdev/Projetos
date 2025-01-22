package br.com.luhf.produto.service;

import java.util.List;
import org.springframework.stereotype.Service;

import br.com.luhf.produto.domain.Produto;
import br.com.luhf.produto.repository.IProdutoRepository;

@Service
public class ProdutoService {
	private final IProdutoRepository repository;

    public ProdutoService(IProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> buscarTodos() {
        return repository.findAll();
    }

    public Produto buscarPorId(String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
    }

    public Produto cadastrar(Produto produto) {
        return repository.save(produto);
    }

    public Produto atualizar(String id, Produto produtoAtualizado) {
    	Produto produto = buscarPorId(id);
    	produto.setNome(produtoAtualizado.getNome());
    	produto.setCodigo(produtoAtualizado.getCodigo());
    	produto.setQuantidade(produtoAtualizado.getQuantidade());
        return repository.save(produto);
    }

    public void deletar(String id) {
        repository.deleteById(id);
    }
}
