package br.com.EduardoPina.screenMatch.dto;

import br.com.EduardoPina.screenMatch.model.Categoria;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record SerieDTO(long id,
                       String titulo,
                       Integer totaltemporadas,
                       Double avaliacao,
                       Categoria genero,
                       String atores,
                       String poster,
                       String sinopse) {

}
