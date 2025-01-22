package br.com.luhf.vendas.online.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.luhf.vendas.online.domain.Venda;
import br.com.luhf.vendas.online.service.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/vendas")
@Tag(name = "Venda", description = "Gerenciamento de vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @GetMapping
    @Operation(summary = "Listar todas as vendas", description = "Retorna uma lista de vendas registradas")
    public List<Venda> listarVendas() {
        return vendaService.listarVendas();
    }

    @PostMapping
    @Operation(summary = "Criar uma nova venda", description = "Registra uma nova venda no sistema")
    public Venda criarVenda(@RequestBody @Valid Venda venda) {
        return vendaService.salvarVenda(venda);
    }
}
