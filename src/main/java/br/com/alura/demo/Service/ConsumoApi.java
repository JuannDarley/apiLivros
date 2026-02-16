package br.com.alura.demo.Service;


import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ConsumoApi {

    public String obterDados(String endereco) {
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(endereco, String.class);
        return json;
    }
}

