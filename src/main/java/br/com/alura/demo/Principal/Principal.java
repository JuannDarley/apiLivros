package br.com.alura.demo.Principal;

import br.com.alura.demo.Service.ConsumoApi;
import br.com.alura.demo.Service.ConverteDados;
import br.com.alura.demo.model.Autor;
import br.com.alura.demo.model.DadosLivros;
import br.com.alura.demo.model.Livro;
import br.com.alura.demo.model.RespostaApi;
import br.com.alura.demo.repository.AutorRepository;
import br.com.alura.demo.repository.LivroRepository;


import java.util.*;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books?search=";

    private List<Livro> livros = new ArrayList<>();

    private LivroRepository livroRepository;
    private AutorRepository autorRepository;



    private Optional<Livro> livroBusca;

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }



    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    #############################
                    
                         MENU LIVROS
                    
                    1 - Buscar Livros
                    2 - Listar Livros
                    3 - Listar Autores
                    4 - Listar Autores Vivos em Determinado Ano
                    5 - Listar Livros em Determinado Idioma
                                      
                    
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
                case 2:
                    listarTodosLivros();
                    break;
                case 3:
                    listarTodosAutores();
                    break;
                case 4:
                    listarAutoresVivosNesteAno();
                    break;
                case 5:
                    listarLivrosNoIdiomaDesejado();
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

    private void listarLivrosNoIdiomaDesejado() {

        System.out.println("Digite a sigla do idioma para pesquisar (ex: pt, en, es):");
        var idioma = leitura.nextLine().toLowerCase();

        List<Livro> livrosPorIdioma = livroRepository.findByIdiomasContaining(idioma);

        if (livrosPorIdioma.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma: " + idioma);
        } else {
            System.out.println("\n--- Livros em " + idioma + " ---");
            livrosPorIdioma.forEach(System.out::println);
        }
    }

    private void listarAutoresVivosNesteAno() {

        System.out.println("Digite o ano para pesquisar:");
        var ano = leitura.nextInt();
        leitura.nextLine(); // limpar buffer

        List<Autor> autores = autorRepository.buscarAutoresVivosEmAno(ano);

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado estava vivo neste ano.");
        } else {
            System.out.println("\n--- Lista de Autores Vivos em " + ano + " ---");
            autores.forEach(a -> System.out.println("Nome: " + a.getNome() +
                    ", Nascimento: " + a.getAnoNascimento() +
                    ", Falecimento: " + (a.getAnoFalecimento() == null ? "Vivo(a)" : a.getAnoFalecimento())));
        }
    }

    private void listarTodosAutores() {
        List<Autor> autores = autorRepository.findAllByOrderByNomeAsc();

        System.out.println("\n--- Lista de Autores ---");
        autores.forEach(a -> {
            System.out.print("Nome: " + a.getNome() +
                    ", Nascimento: " + a.getAnoNascimento());


            if (a.getAnoFalecimento() != null) {
                System.out.println(", Falecimento: " + a.getAnoFalecimento());
            } else {
                System.out.println(", Falecimento: Vivo(a)");
            }
        });

    }

    private void listarTodosLivros() {
        livros = livroRepository.findAll();
        livros.stream()
                .sorted(Comparator.comparing(Livro::getTitulo))
                .forEach(System.out::println);
    }

    private void buscarLivro() {
        DadosLivros dados = getDadosLivros();

        if (dados != null) {

            Optional<Livro> livroExistente = livroRepository.findByTituloIgnoreCase(dados.titulo());

            if (livroExistente.isPresent()) {
                System.out.println("O livro '" + dados.titulo() + "' já está cadastrado.");
                System.out.println(livroExistente.get());
            } else {

                Livro novoLivro = new Livro(dados);
                livroRepository.save(novoLivro);
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