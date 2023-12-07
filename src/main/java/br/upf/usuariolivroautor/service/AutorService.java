package br.upf.usuariolivroautor.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.upf.usuariolivroautor.dto.AutorDTO;
import br.upf.usuariolivroautor.repository.AutorRepository;

@Service
public class AutorService {
	
	@Autowired
	private AutorRepository autorRepository;

	/**
	 * Metodo serve pra salvar ou atualizar um objeto
	 * @param dto
	 * @return
	 */
	public AutorDTO salvar(AutorDTO dto) {
		return autorRepository.save(dto);
	}
	
	public List<AutorDTO> listarTodos(){
		return autorRepository.findAll();
	}
	
	public Optional<AutorDTO> buscarPorId(Long id) {
		return autorRepository.findById(id);
	}
	
	public void removerPorId(Long id) {
		autorRepository.deleteById(id);
	}
	
	public List<AutorDTO> buscarPorParteNome(String nome){
		return autorRepository.findByNomeContaining(nome);
	}
	
	
	public List<AutorDTO> buscarPorDataNascimento(Date dataNascimento){		
		return autorRepository.findByPorDataNascimento(dataNascimento);
	}
}
