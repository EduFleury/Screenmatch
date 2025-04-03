package br.com.EduardoPina.screenMatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosService(@JsonAlias("Title") String titulo,
                           @JsonAlias("totalSeasons") Integer totaltemporadas,
                           @JsonAlias("imdbRating") String avaliacao) {
}


