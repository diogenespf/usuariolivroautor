package br.upf.usuariolivroautor.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.upf.usuariolivroautor.dto.UsuarioDTO;

public interface UsuarioRepository extends JpaRepository<UsuarioDTO, Long> {
	
	public UsuarioDTO findByEmail(String email);
	
	//Querie utilizando Spring DATA
	public List<UsuarioDTO> findByNomeContaining(String name);
	
	//Querie otimizada utilizando JPQL
	@Query("SELECT u FROM UsuarioDTO u WHERE u.senha =:senha ORDER BY u.id DESC")
	public List<UsuarioDTO> findByPorSenha(@Param("senha") String senha);
}
