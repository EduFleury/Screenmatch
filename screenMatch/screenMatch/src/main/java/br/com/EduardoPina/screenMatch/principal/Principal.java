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

       Optional<Serie> serie = series.stream().filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
               .findFirst();

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

}
