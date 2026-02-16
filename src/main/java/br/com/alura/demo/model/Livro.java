package br.com.alura.demo.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;


    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores;


    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> idiomas;

    private Integer numeroDownloads;


    public Livro() {}


    public Livro(DadosLivros dadosLivro) {
        this.titulo = dadosLivro.titulo();
        this.idiomas = dadosLivro.idiomas();
        this.numeroDownloads = dadosLivro.numeroDownloads();


        this.autores = dadosLivro.autores().stream()
                .map(dadosAutor -> new Autor(dadosAutor))
                .collect(Collectors.toList());
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public List<Autor> getAutores() { return autores; }
    public void setAutores(List<Autor> autores) { this.autores = autores; }

    public List<String> getIdiomas() { return idiomas; }
    public void setIdiomas(List<String> idiomas) { this.idiomas = idiomas; }

    public Integer getNumeroDownloads() { return numeroDownloads; }
    public void setNumeroDownloads(Integer numeroDownloads) { this.numeroDownloads = numeroDownloads; }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", Título='" + titulo + '\'' +
                ", Autores=" + autores +
                ", Idiomas=" + idiomas +
                ", Downloads=" + numeroDownloads +
                '}';
    }
}