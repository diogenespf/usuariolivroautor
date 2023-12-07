package br.upf.usuariolivroautor.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.upf.usuariolivroautor.dto.AutorDTO;

public interface AutorRepository extends JpaRepository<AutorDTO, Long> {
	
	//Querie utilizando Spring DATA
	public List<AutorDTO> findByNomeContaining(String name);

	//Querie otimizada utilizando JPQL
	@Query("SELECT u FROM AutorDTO u WHERE u.dataNascimento =:dataNascimento ORDER BY u.id DESC")
	public List<AutorDTO> findByPorDataNascimento(@Param("dataNascimento") Date dataNascimento);
}
