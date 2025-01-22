package br.com.luhf.vendas.online;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import br.com.luhf.vendas.online.controller.ClienteResource;
import br.com.luhf.vendas.online.domain.Cliente;
import br.com.luhf.vendas.online.usecase.BuscaCliente;
import br.com.luhf.vendas.online.usecase.CadastroCliente;

public class ClienteResourceTest {

    @InjectMocks
    private ClienteResource clienteResource;

    @Mock
    private BuscaCliente buscaCliente;

    @Mock
    private CadastroCliente cadastroCliente;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(clienteResource).build();  // Setup MockMvc
    }

    @Test
    public void buscarPorId() throws Exception {
        Cliente cliente1 = Cliente.builder().id("1").nome("Rodrigo 1").build();

        when(buscaCliente.buscarPorId("1")).thenReturn(cliente1);

        mockMvc.perform(get("/cliente/{id}", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(cliente1.getId()))
               .andExpect(jsonPath("$.nome").value(cliente1.getNome()));
    }
}
