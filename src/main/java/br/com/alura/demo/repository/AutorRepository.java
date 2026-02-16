package br.com.alura.demo.repository;

import br.com.alura.demo.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    List<Autor> findAllByOrderByNomeAsc();
}