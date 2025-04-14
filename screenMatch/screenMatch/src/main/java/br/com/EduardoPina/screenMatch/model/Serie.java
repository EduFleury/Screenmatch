package br.com.EduardoPina.screenMatch.model;

import br.com.EduardoPina.screenMatch.service.ConsultaChatGPT;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.OptionalDouble;

public class Serie {
    private String titulo;
    private Integer totaltemporadas;
    private Double avaliacao;
    private Categoria genero;
    private String atores;
    private String poster;
    private String sinopse;

    public Serie(DadosSerie serie){
        this.titulo = serie.titulo();
        this.totaltemporadas = serie.totaltemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(serie.avaliacao())).orElse(0);
        this.genero = Categoria.fromString(serie.genero().split(",")[0].trim());
        this.atores = serie.atores();
        this.poster = serie.poster();
        this.sinopse = ConsultaChatGPT.obterTraducao(serie.sinopse()).trim();

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
}
