package br.com.EduardoPina.screenMatch.repository;

import br.com.EduardoPina.screenMatch.model.Categoria;
import br.com.EduardoPina.screenMatch.model.Episodio;
import br.com.EduardoPina.screenMatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long>{

   Optional<Serie> findByTituloContainingIgnoreCase(String nomeserie);
   List<Serie> findByAtoresContainingIgnoreCase(String nomeAtor);

   List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);


   List<Serie> findTop5ByOrderByAvaliacaoDesc();

   List<Serie> findByGenero(Categoria categoria);

   List<Serie> findBytotaltemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer temporadas, Double avaliacao);

   @Query("select s from Serie s WHERE s.totaltemporadas <= :Totaltemporadas AND s.avaliacao >= :avaliacao")
   List<Serie> seriePorTemporadaEAvaliacao(Integer Totaltemporadas, Double avaliacao);

   @Query("select e from Serie s Join s.episodios e WHERE e.titulo ILIKE %:trechosEpisodios% ")
   List<Episodio> episodiosPorTrecho(String trechosEpisodios);

   @Query("select e from Serie s Join s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5 ")
   List<Episodio> topEpisodiosPorSerie(Serie serie);

   @Query("select e from Serie s Join s.episodios e WHERE s = :serie AND YEAR(e.datalancamento) >= :anoLancamento ")
   List<Episodio> episodiosPorAnoESerie(Serie serie, int anoLancamento);

   @Query("SELECT s FROM Serie s " +
           "JOIN s.episodios e " +
           "GROUP BY s " +
           "ORDER BY MAX(e.datalancamento) DESC LIMIT 5")
   List<Serie> encontrarEpisodiosMaisRecentes();

   @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numero")
   List<Episodio> obterEpisodioPorTemporada(Long id, Long numero);
}
