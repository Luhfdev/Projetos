package br.com.luhf.vendas.online.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Document(collection = "vendas")
public class Venda {

    @Id
    private String id;
    
    @NotBlank(message = "O id do Cliente é obrigatório")
    private String clienteId;
    
    @NotBlank(message = "O codigo do produto é obrigatório")
    private String produtoCodigo;
    
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
    private int quantidade;
    
    private double total;
}
