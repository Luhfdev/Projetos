package br.com.luhf.vendas.online.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.luhf.vendas.online.domain.Venda;
import br.com.luhf.vendas.online.repository.IVendaRepository;

@Service
public class VendaService {
	
    @Autowired
    private IVendaRepository vendaRepository;
    
    @Autowired
    private MicroserviceClient microserviceClient;

    public List<Venda> listarVendas() {
        return vendaRepository.findAll();
    }

    public Venda salvarVenda(Venda venda) {
        if (!microserviceClient.verificarCliente(venda.getClienteId())) {
            throw new IllegalArgumentException("Cliente inválido.");
        }

        if (!microserviceClient.verificarProduto(venda.getProdutoCodigo())) {
            throw new IllegalArgumentException("Produto inválido.");
        }

        return vendaRepository.save(venda);
    }
}
