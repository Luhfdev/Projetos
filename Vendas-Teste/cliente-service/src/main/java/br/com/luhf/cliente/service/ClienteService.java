package br.com.luhf.cliente.service;

import java.util.List;
import org.springframework.stereotype.Service;

import br.com.luhf.cliente.domain.Cliente;
import br.com.luhf.cliente.repository.IClienteRepository;

@Service
public class ClienteService {

    private final IClienteRepository repository;

    public ClienteService(IClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> buscarTodos() {
        return repository.findAll();
    }

    public Cliente buscarPorId(String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
    }

    public Cliente cadastrar(Cliente cliente) {
        return repository.save(cliente);
    }

    public Cliente atualizar(String id, Cliente clienteAtualizado) {
        Cliente cliente = buscarPorId(id);
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setCpf(clienteAtualizado.getCpf());
        cliente.setEmail(clienteAtualizado.getEmail());
        return repository.save(cliente);
    }

    public void deletar(String id) {
        repository.deleteById(id);
    }
}
