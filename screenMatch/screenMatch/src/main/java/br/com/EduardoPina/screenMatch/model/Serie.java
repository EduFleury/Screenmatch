package br.com.EduardoPina.screenMatch.model;

import br.com.EduardoPina.screenMatch.service.ConsultaChatGPT;
import br.com.EduardoPina.screenMatch.service.ConsultaMyMemory;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String titulo;
    private Integer totaltemporadas;
    private Double avaliacao;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String atores;
    private String poster;
    private String sinopse;

    @Transient
    private List<Episodio> episodios = new ArrayList<>();

    public Serie(){}

    public Serie(DadosSerie serie){
        this.titulo = serie.titulo();
        this.totaltemporadas = serie.totaltemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(serie.avaliacao())).orElse(0);
        this.genero = Categoria.fromString(serie.genero().split(",")[0].trim());
        this.atores = serie.atores();
        this.poster = serie.poster();
        this.sinopse = ConsultaMyMemory.obterTraducao(serie.sinopse()).trim();

    }

    @Override
    public String toString() {
        return "Serie{" +
                " genero: "+ genero +
                ", titulo: '" + titulo +
                ", totaltemporadas: " + totaltemporadas +
                ", avaliacao: " + avaliacao +
                ", atores: " + atores +
                ", poster: " + poster +
                ", sinopse: " + sinopse +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotaltemporadas() {
        return totaltemporadas;
    }

    public void setTotaltemporadas(Integer totaltemporadas) {
        this.totaltemporadas = totaltemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        this.episodios = episodios;
    }
}
