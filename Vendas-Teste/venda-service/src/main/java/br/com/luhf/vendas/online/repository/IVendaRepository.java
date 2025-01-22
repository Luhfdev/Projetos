package br.com.luhf.vendas.online.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.luhf.vendas.online.domain.Venda;

public interface IVendaRepository extends MongoRepository<Venda, String> {
}
