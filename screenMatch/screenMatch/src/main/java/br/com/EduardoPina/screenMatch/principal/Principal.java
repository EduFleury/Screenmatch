package br.com.EduardoPina.screenMatch.principal;

import br.com.EduardoPina.screenMatch.model.*;
import br.com.EduardoPina.screenMatch.repository.SerieRepository;
import br.com.EduardoPina.screenMatch.service.ConsumoAPI;
import br.com.EduardoPina.screenMatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    Scanner leitura = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=f459e5c5";
    ConsumoAPI consumoAPI = new ConsumoAPI();
    ConverteDados converteDados = new ConverteDados();
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repository;
    List<Serie> series = new ArrayList<>();

    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibirMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por titulo
                    5 - Buscar série por Ator
                    6 - Buscar Top 5 séries
                    7 - Buscar séries por categoria
                    8 - Buscar séries por temporadas / Avaliacao
                    9 - Buscar episodios por trecho do nome
                    10- Top Episodios por serie
                    11- Episodios a partir de uma data
                    

                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    ListarSerieBuscada();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAutor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    buscarSeriesPorTemporadaAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosDepoisDeUmaData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }

    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repository.save(serie);
//        dadosSeries.add(dados);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = leitura.nextLine();
        var json = consumoAPI.obterDados(ENDERECO+nomeSerie.replace(" ", "+")+API_KEY);
        DadosSerie dados = converteDados.ObterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        ListarSerieBuscada();
        System.out.println("Escolha uma serie pelo nome: ");
       var nomeSerie = leitura.nextLine();

       Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

       if(serie.isPresent()){
            var serieEncontrada = serie.get();
           List<DadosTemporada> temporada = new ArrayList<>();

           for(int i =1; i<=serieEncontrada.getTotaltemporadas(); i++){
               var json = consumoAPI.obterDados(ENDERECO+serieEncontrada.getTitulo().replace(" ", "+")+"&season="+i+API_KEY);
               DadosTemporada dadosTemporada = converteDados.ObterDados(json, DadosTemporada.class);
               temporada.add(dadosTemporada);
           }
           temporada.forEach(System.out::println);
           List<Episodio> episodios = temporada.stream()
                   .flatMap(d -> d.episodios().stream()
                           .map(e -> new Episodio(d.numero(), e)))
                           .collect(Collectors.toList());
           serieEncontrada.setEpisodios(episodios);
           repository.save(serieEncontrada);
       }else{
           System.out.println("Serie não encontrada!");
       }
    }

    private void ListarSerieBuscada() {
          series = repository.findAll();
//        dadosSeries.stream().map(d -> new Serie(d)).collect(Collectors.toList());
        series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);
    }

    private void buscarSeriePorTitulo(){
        System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = leitura.nextLine();

        serieBusca = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBusca.isPresent()){
            System.out.println("Dados da série: "+serieBusca.get());
        }else{
            System.out.println("Série não encontrada.");
        }
    }

    private void buscarSeriePorAutor(){
        System.out.println("Escolha uma serie pelo nome do ator: ");
        var nomeAtor = leitura.nextLine();

//        List<Serie> serieBuscada = repository.findByAtoresContainingIgnoreCase(nomeAtor);
//
//        System.out.println("Dados das Séries em que "+nomeAtor+" trabalhou.");
//        serieBuscada.forEach(s -> System.out.println(s.getTitulo() + " Avaliação: "+s.getAvaliacao()));
        System.out.println("Avaliação a partir de que valor? ");
        var avaliacao = leitura.nextDouble();

        List<Serie> melhoresSeries = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);

        System.out.println("Dados das Séries em que "+nomeAtor+" trabalhou.");
        melhoresSeries.forEach(s -> System.out.println(s.getTitulo() + " Avaliação: "+s.getAvaliacao()));

    }

    private void buscarTop5Series(){
        List<Serie> melhoresSeries = repository.findTop5ByOrderByAvaliacaoDesc();

        System.out.println("Dados das Top 5 melhores Séries.");
        melhoresSeries.forEach(s -> System.out.println(s.getTitulo() + " Avaliação: "+s.getAvaliacao()));
    }

    private void buscarSeriesPorCategoria(){
        System.out.println("Escolha uma categoria: ");
        var nomeGenero = leitura.nextLine();

        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);

        System.out.println("Dados das Séries por Categoria ("+categoria+")");
        seriesPorCategoria.forEach(s -> System.out.println(s.getTitulo() + " Avaliação: "+s.getAvaliacao()));
    }

    private void buscarSeriesPorTemporadaAvaliacao(){
        System.out.println("Qual numero de temporadas maximo que deseja? ");
        var temporadas = leitura.nextInt();

        System.out.println("Avaliação maior que? ");
        var avaliacao = leitura.nextDouble();

//        List<Serie> seriesBuscadas = repository.findBytotaltemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(temporadas, avaliacao);
        List<Serie> seriesBuscadas = repository.seriePorTemporadaEAvaliacao(temporadas, avaliacao);


        System.out.println("Dados das Séries Encontradas: ");
        seriesBuscadas.forEach(s -> System.out.println(s.getTitulo() + " Avaliação: "+s.getAvaliacao()));

    }

    private void buscarEpisodioPorTrecho(){
        System.out.println("Digite o trecho do nome do episodio:  ");
        var trechosEpisodios = leitura.nextLine();

        List<Episodio> episodiosEncontrados = repository.episodiosPorTrecho(trechosEpisodios);

        System.out.println("Dados dos episodios: ");
        episodiosEncontrados.forEach(s -> System.out.println("Serie "+ s.getSerie().getTitulo() +" "+s.getTitulo() + " Avaliação: "+s.getAvaliacao()));
    }

    private void topEpisodiosPorSerie(){
       buscarSeriePorTitulo();
       if(serieBusca.isPresent()){
           Serie serie = serieBusca.get();
           List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);

           System.out.println("Dados dos episodios: ");
           topEpisodios.forEach(e -> System.out.println("Serie "+ e.getSerie().getTitulo() +" "+e.getTitulo() + " Avaliação: "+e.getAvaliacao()));

       }
    }

    private void buscarEpisodiosDepoisDeUmaData(){
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            System.out.println("Digite o ano limite de lançamento: ");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();

            Serie serie = serieBusca.get();
            List<Episodio> episodiosAno = repository.episodiosPorAnoESerie(serie, anoLancamento);

            System.out.println("Dados dos episodios: ");
            episodiosAno.forEach(e -> System.out.println("Serie "+ e.getSerie().getTitulo() +" "+e.getTitulo() + " Avaliação: "+e.getAvaliacao()));

        }
    }

}
