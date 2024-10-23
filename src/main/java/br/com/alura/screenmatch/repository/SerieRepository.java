package br.com.alura.screenmatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import br.com.alura.screenmatch.model.Serie;
//especificar qual entidade e o tipo de id
public interface SerieRepository extends JpaRepository<Serie, Long>{
    Serie findByTituloContainingIgnoreCase(String titulo);
}
