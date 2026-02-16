package br.com.alura.demo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivros(@JsonAlias("title") String titulo,
                          @JsonAlias("authors") List<DadosAutor> autores,
                          @JsonAlias("languages") List<String> idiomas,
                          @JsonAlias("download_count") Integer numeroDownloads)
                           {
}
