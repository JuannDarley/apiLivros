package br.com.alura.demo.Principal;

import br.com.alura.demo.Service.ConsumoApi;
import br.com.alura.demo.Service.ConverteDados;
import br.com.alura.demo.model.DadosLivros;
import br.com.alura.demo.model.Livro;
import br.com.alura.demo.model.RespostaApi;
import br.com.alura.demo.repository.LivroRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books?search=";

    private List<Livro> livros = new ArrayList<>();

    private LivroRepository repositorio;

    private Optional<Livro> livroBusca;

    public Principal(LivroRepository repositorio) {
        this.repositorio = repositorio;
    }



    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    #############################
                    
                         MENU LIVROS
                    
                    1 - Buscar Livros
                                      
                    
                    0 - Sair                                 
                    
                    #############################
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivro();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarLivro() {
        DadosLivros dados = getDadosLivros();

        if (dados != null) {

            Optional<Livro> livroExistente = repositorio.findByTituloIgnoreCase(dados.titulo());

            if (livroExistente.isPresent()) {
                System.out.println("O livro '" + dados.titulo() + "' já está cadastrado.");
                System.out.println(livroExistente.get());
            } else {

                Livro novoLivro = new Livro(dados);
                repositorio.save(novoLivro);
                System.out.println("Livro salvo com sucesso:");
                System.out.println(novoLivro);
            }
        }
    }

    private DadosLivros getDadosLivros(){
        System.out.println("Digite o nome da livro para busca");
        var nomeLivro = leitura.nextLine();

        String url = ENDERECO + nomeLivro.replace(" ", "+");
        System.out.println("Chamando URL: " + url);

        var json = consumo.obterDados(url);


        RespostaApi resposta = conversor.obterDados(json, RespostaApi.class);


        if (!resposta.livros().isEmpty()) {

            DadosLivros dados = resposta.livros().get(0);
            return dados;
        } else {
            System.out.println("Livro não encontrado.");
            return null;
        }
    }
}