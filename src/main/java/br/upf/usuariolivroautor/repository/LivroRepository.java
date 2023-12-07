package br.upf.usuariolivroautor.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.upf.usuariolivroautor.dto.LivroDTO;


public interface LivroRepository extends JpaRepository<LivroDTO, Long> {
	
	//Querie utilizando Spring DATA
	public List<LivroDTO> findByNomeContaining(String name);
	
	//Querie otimizada utilizando SQL Nativo
	@Query(nativeQuery = true, value = "select u.* from tb_livro u inner join tb_autor d on d.id = u.autor_id "
		+ "where u.autor_id = :autorId order by u.livro_nome asc")
	public List<LivroDTO> findByPorAutorId(@Param("autorId") Long autorId);
}
