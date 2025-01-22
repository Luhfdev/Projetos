package br.com.luhf.produto.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.luhf.produto.domain.Produto;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Document(collection = "produtos")
public class Produto {
    @Id
    private String id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Codigo é obrigatório")
    private String codigo;

    private String quantidade;
}

