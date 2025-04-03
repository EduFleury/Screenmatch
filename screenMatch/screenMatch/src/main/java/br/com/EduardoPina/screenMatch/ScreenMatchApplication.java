package br.com.EduardoPina.screenMatch;

import br.com.EduardoPina.screenMatch.model.DadosEpisodios;
import br.com.EduardoPina.screenMatch.model.DadosService;
import br.com.EduardoPina.screenMatch.service.ConsumoAPI;
import br.com.EduardoPina.screenMatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args){

		ConsumoAPI consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&Season=1&apikey=f459e5c5");
//		System.out.println(json);

		ConverteDados converteDados = new ConverteDados();
		DadosService dadosService = converteDados.ObterDados(json, DadosService.class);

		System.out.println(dadosService);

		json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&Season=1&Episode=2&apikey=f459e5c5");

		DadosEpisodios dadosEpisodios = converteDados.ObterDados(json, DadosEpisodios.class);
		System.out.println(dadosEpisodios);

	}
}
