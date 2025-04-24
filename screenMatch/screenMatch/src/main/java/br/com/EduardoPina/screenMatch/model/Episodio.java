package br.com.EduardoPina.screenMatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodios")
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private Double avaliacao;
    private LocalDate datalancamento;
    @ManyToOne
    private Serie serie;

    @Override
    public String toString() {
        return "Episodio{" +
                "temporada= " + temporada +
                ", titulo= '" + titulo + '\'' +
                ", numeroEpisodio= " + numeroEpisodio +
                ", avaliacao= " + avaliacao +
                ", datalancamento= " + datalancamento +
                '}';
    }

    public Episodio(){}

    public Episodio(Integer numero, DadosEpisodios d) {
        this.temporada = numero;
        this.titulo = d.titulo();
        this.numeroEpisodio = d.numero();
        try{
            this.avaliacao = Double.valueOf(d.avaliacao());
        }catch (NumberFormatException ex){
            this.avaliacao = Double.valueOf(0);
        }

        try{
            this.datalancamento = LocalDate.parse(d.datalancamento());
        }catch (DateTimeParseException px){
            this.datalancamento = null;
        }
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getnumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setnumeroEpisodio(Integer numero) {
        this.numeroEpisodio = numero;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDatalancamento() {
        return datalancamento;
    }

    public void setDatalancamento(LocalDate datalancamento) {
        this.datalancamento = datalancamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
