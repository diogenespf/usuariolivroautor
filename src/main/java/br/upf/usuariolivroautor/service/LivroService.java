package br.upf.usuariolivroautor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.upf.usuariolivroautor.dto.LivroDTO;
import br.upf.usuariolivroautor.repository.LivroRepository;


@Service
public class LivroService {

	@Autowired
	private LivroRepository livroRepository;
	
	/**
	 * Metodo serve pra salvar ou atualizar um objeto
	 * @param dto
	 * @return
	 */
	public LivroDTO salvar(LivroDTO dto) {
		return livroRepository.save(dto);
	}
	
	public List<LivroDTO> listarTodos(){
		return livroRepository.findAll();
	}
	
	public Optional<LivroDTO> buscarPorId(Long id) {
		return livroRepository.findById(id);
	}
	
	public void removerPorId(Long id) {
		livroRepository.deleteById(id);
	}
	
	public List<LivroDTO> buscarPorParteNome(String nome){
		return livroRepository.findByNomeContaining(nome);
	}
	
	public List<LivroDTO> buscarPorAutorId(Long autorId) {
		return livroRepository.findByPorAutorId(autorId);
	}
}
