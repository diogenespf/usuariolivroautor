package br.upf.usuariolivroautor.repository;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public abstract class UsuarioRepositoryImpl implements UsuarioRepository {
	
	@PersistenceContext
	private EntityManager em;
}
