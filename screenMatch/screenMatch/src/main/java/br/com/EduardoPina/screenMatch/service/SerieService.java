package br.com.EduardoPina.screenMatch.service;

import br.com.EduardoPina.screenMatch.dto.EpisodioDTO;
import br.com.EduardoPina.screenMatch.dto.SerieDTO;
import br.com.EduardoPina.screenMatch.model.Episodio;
import br.com.EduardoPina.screenMatch.model.Serie;
import br.com.EduardoPina.screenMatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository serieRepository;

    public List<SerieDTO> obterTodasAsSeries(){
        return converteDados(serieRepository.findAll());
    }

    public List<SerieDTO> obterTop5Series() {

        return converteDados(serieRepository.findTop5ByOrderByAvaliacaoDesc());


    }
    private List<SerieDTO> converteDados(List<Serie> series){

        return series.stream()
                .map(s -> new SerieDTO(s.getId(),
                        s.getTitulo(),
                        s.getTotaltemporadas(),
                        s.getAvaliacao(),
                        s.getGenero(),
                        s.getAtores(),
                        s.getPoster(),
                        s.getSinopse())).collect(Collectors.toList());
    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(serieRepository.encontrarEpisodiosMaisRecentes());
    }

    public SerieDTO obterPorId(Long id) {

        Optional<Serie> serie = serieRepository.findById(id);;

        if(serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(),
                    s.getTitulo(),
                    s.getTotaltemporadas(),
                    s.getAvaliacao(),
                    s.getGenero(),
                    s.getAtores(),
                    s.getPoster(),
                    s.getSinopse());
        }else{
            return null;
        }
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {

        Optional<Serie> serie = serieRepository.findById(id);;

        if(serie.isPresent()){
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getnumeroEpisodio(), e.getTitulo()))
                    .collect(Collectors.toList());
        }else{
            return null;
        }
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {

        return serieRepository.obterEpisodioPorTemporada(id, numero).stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getnumeroEpisodio(), e.getTitulo()))
                .collect(Collectors.toList());
    }
}
