package br.com.EduardoPina.screenMatch.principal;

import br.com.EduardoPina.screenMatch.model.DadosEpisodios;
import br.com.EduardoPina.screenMatch.model.DadosService;
import br.com.EduardoPina.screenMatch.model.DadosTemporada;
import br.com.EduardoPina.screenMatch.model.Episodio;
import br.com.EduardoPina.screenMatch.service.ConsumoAPI;
import br.com.EduardoPina.screenMatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    Scanner sc = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=f459e5c5";
    ConsumoAPI consumoAPI = new ConsumoAPI();
    ConverteDados converteDados = new ConverteDados();

    public void exibirMenu(){

        System.out.println("Digite o nome da série para busca: ");
        var serie = sc.nextLine();

        var json = consumoAPI.obterDados(ENDERECO+serie.replace(" ", "+")+API_KEY);

        converteDados = new ConverteDados();
        DadosService dadosService = converteDados.ObterDados(json, DadosService.class);

        //Dados Serie
        System.out.println(dadosService);

        List<DadosTemporada> temporadaList = new ArrayList<>();

		for(int i = 1; i <= dadosService.totaltemporadas(); i++){
            //Dados da temporada
			json = consumoAPI.obterDados(ENDERECO+serie.replace(" ", "+")+"&Season="+i+API_KEY);

			DadosTemporada dadosTemporada = converteDados.ObterDados(json, DadosTemporada.class);
			temporadaList.add(dadosTemporada);
		}

        //dados episodios
        temporadaList.forEach( t -> t.episodios().forEach( e -> System.out.println(e.titulo())));

        List<DadosEpisodios> dadosEpisodios = temporadaList.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("Top 10 episódios");
        dadosEpisodios.stream()
                .filter( e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .peek( e -> System.out.println("Primeiro Filtro" + e))
                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
                .peek( e -> System.out.println("Filtro(Ordenação)" + e))
                .limit(10)
                .peek( e -> System.out.println("Filtro(limite)" + e))
                .map(e -> e.titulo().toUpperCase())
                .forEach(System.out::println);

        //Episodios
        System.out.println("Top Episódios");
        List<Episodio> episodios = temporadaList.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("Busque por um episódio: ");
        var trechoTitulo = sc.nextLine();

        Optional<Episodio> EpisodioTitulo = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        if(EpisodioTitulo.isPresent()){
            System.out.println("Encontrei o episódio");
            System.out.println("Temporada: "+EpisodioTitulo.get().getTemporada());

        }else{
            System.out.println("Não econtrei o episódio");
        }

        //buscando episodios a partir de uma data
        System.out.println("A partir de que ano você deseja ver os episódios?");
        var ano = sc.nextInt();
        sc.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        //formatador de datas
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream().filter(e -> e.getDatalancamento() != null && e.getDatalancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println("Temporada: "+e.getTemporada()
                                                +" Episódios: "+e.getTitulo()
                                                +"Data Lançamento: "+e.getDatalancamento().format(formatador)));

        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream().
                filter(e -> e.getAvaliacao() > 0.0).
                collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacaoPorTemporada);

        DoubleSummaryStatistics est = episodios.stream().
                filter(e -> e.getAvaliacao() > 0.0).
                collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println(est);

        System.out.println("Média: "+est.getAverage());


    }

}
