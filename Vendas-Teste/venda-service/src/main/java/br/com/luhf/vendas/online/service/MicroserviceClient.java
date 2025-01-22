package br.com.luhf.vendas.online.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MicroserviceClient {

    private final RestTemplate restTemplate;

    @Value("${cliente-service.url}")
    private String clienteServiceUrl;

    @Value("${produto-service.url}")
    private String produtoServiceUrl;

    public MicroserviceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean verificarCliente(String clienteId) {
        String url = clienteServiceUrl + "/" + clienteId;
        try {
            restTemplate.getForObject(url, Void.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verificarProduto(String produtoCodigo) {
        String url = produtoServiceUrl + "/" + produtoCodigo;
        try {
            restTemplate.getForObject(url, Void.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
