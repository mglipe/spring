package br.com.alura.screenmatch.model;


import java.util.List;
import java.util.ArrayList;
import java.util.OptionalDouble;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    private Integer totalTemporadas;

    private Double avaliacao;
    @Column(name = "atores")
    private String ator;
    @Enumerated(EnumType.STRING)
    private Categoria genero;

    private String poster;

    private String sinopse;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> listaEpisodios = new ArrayList<>();

    public Serie(){

    }

    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
        this.ator = dadosSerie.ator();
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.poster = dadosSerie.poster();
        this.sinopse = dadosSerie.sinopse();
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }
    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }
    public Double getAvaliacao() {
        return avaliacao;
    }
    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }
    public String getAtor() {
        return ator;
    }
    public void setAtor(String ator) {
        this.ator = ator;
    }
    public Categoria getGenero() {
        return genero;
    }
    public void setGenero(Categoria genero) {
        this.genero = genero;
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
    public List<Episodio> getListaEpisodios() {
        return listaEpisodios;
    }

    public void setListaEpisodios(List<Episodio> listaEpisodios) {
        listaEpisodios.forEach(e -> e.setSerie(this));
        this.listaEpisodios = listaEpisodios;
    }

    @Override
    public String toString(){
        return
                "Titulo: " +this.getTitulo() +
                "\nTotal de temporadas: " + this.getTotalTemporadas() +
                "\nAvaliacao: " + this.getAvaliacao() +
                "\nAtor: " + this.getAtor() +
                "\nGenero: " + this.getGenero() +
                "\nPoster: " + this.getPoster() +
                "\nSinopse: " + this.getSinopse()+
                "\nEpisodios: " + this.listaEpisodios;
    }



}
