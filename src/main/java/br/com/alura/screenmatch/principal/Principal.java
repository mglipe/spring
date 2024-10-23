package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import java.util.stream.Collectors;

import org.hibernate.sql.ast.tree.expression.Collation;



public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private SerieRepository repository;

    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repository){
        this.repository = repository;
    }

    public void exibeMenu() {
        int opcao = -1;
        while(opcao != 0){
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Lista séries buscadas
                    4 - Buscar série por título
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    this.buscarSerieWeb();
                    break;
                case 2:
                    this.buscarEpisodioPorSerie();
                    break;
                case 3:
                    this.listarSeriesBuscada();
                    break;
                case 4:
                    this.buscarSeriePorTitulo();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        // this.listaSeries.add(dados);
        Serie serie = new Serie(dados);
        this.repository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscada();
        System.out.println("Buscar episódios por nome da série: ");
        var tituloSerie = this.leitura.nextLine();


        Optional<Serie> serie = series.stream()
            .filter(s -> s.getTitulo().toLowerCase().contains(tituloSerie.toLowerCase()))
            .findFirst();

        if(serie.isPresent()){

            //fazendo referência para serie
            var serieEncontrada = serie.get();

            List<DadosTemporada> temporadas = new ArrayList<>();
    
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }

            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                    .map(e -> new Episodio(t.numero(), e)))
                .collect(Collectors.toList());

                serieEncontrada.setListaEpisodios(episodios);
                repository.save(serieEncontrada);
        }else{
            System.out.println("Serie não encontrada");
        }

        
            
    }

    public void listarSeriesBuscada(){
        this.series = repository.findAll();
            
        series.stream()
            .sorted(Comparator.comparing(Serie::getGenero))
            .forEach(System.out::println);
        
    }


    public void buscarSeriePorTitulo(){
        System.out.println("Escolha uma série pelo nome: ");
        var tituloSerie = this.leitura.nextLine();

        try{
            Serie serieBuscada = this.repository.findByTituloContainingIgnoreCase(tituloSerie);
            System.out.println(serieBuscada);
        }catch(IllegalStateException e){
            System.out.println("Error: Série não encontrada" + e.getMessage());
        }
    }
}