package br.com.luhf.produto.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.luhf.produto.domain.Produto;

public interface IProdutoRepository extends MongoRepository<Produto, String> {
    Optional<Produto> findByCodigo(String Codigo);
}
