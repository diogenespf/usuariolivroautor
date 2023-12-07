package br.upf.usuariolivroautor.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import br.upf.usuariolivroautor.dto.LivroDTO;
import br.upf.usuariolivroautor.service.LivroService;
import br.upf.usuariolivroautor.utils.TokenJWT;

@RestController
@RequestMapping(value = "/usuariolivroautor/livro")
public class LivroController {

	@Autowired
	private LivroService livroService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping(value = "/inserir")
	@ResponseStatus(HttpStatus.CREATED)
	public LivroDTO inserir(@RequestBody LivroDTO livroDTO, @RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token); 
		return livroService.salvar(livroDTO);
	}
	
	@GetMapping(value = "/listarTodos")
	@ResponseStatus(HttpStatus.OK)
	public List<LivroDTO> listarTodos(@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return livroService.listarTodos();
	}
	
	@GetMapping(value = "/buscarPorId")
	@ResponseStatus(HttpStatus.OK)
	public LivroDTO buscarPorId(@RequestHeader(value = "id") Long id,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return livroService.buscarPorId(id)
				// caso o livro n foi encontrado...
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Livro não encontrado."));
	}
	
	@DeleteMapping(value = "/delete")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerLivro(@RequestHeader(value = "id") Long id,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		livroService.buscarPorId(id) //antes de deletar, busca na base o livro...
			.map(livro -> { //definindo a variavel no map
				livroService.removerPorId(livro.getId()); // caso encontre o livro
				return Void.TYPE;
				// caso n encontre, retorna o status
			}).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Livro não encontrado."));			
	}
	
	/** 
	 * Para implementar o atualizar, é necessario incluir o
	 * metodo bean modelMapper() na classe ...Apllication.java
	 * @param livroDTO
	 * @param id
	 */
	@PutMapping(value = "/atualizar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizar(@RequestBody LivroDTO livroDTO,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		livroService.buscarPorId(livroDTO.getId()).map(livroBase -> {
			//recurso do modelMap que verifica o que esta no parametro para
			//atualizar na base.
			//esse recurso necessita de incluir a dependencia modelMapper no pom.xml
			modelMapper.map(livroDTO, livroBase);
			livroService.salvar(livroBase); //salvar os itens alterados
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
				"Livro não encontrado."));		
	}
	
	@GetMapping(value = "/buscarPorParteNome")
	@ResponseStatus(HttpStatus.OK)
	public List<LivroDTO> buscarPorParteNome(@RequestHeader(value = "nome") String nome,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return livroService.buscarPorParteNome(nome);
	}
	
	@GetMapping(value = "/buscarPorAutorId")
	@ResponseStatus(HttpStatus.OK)
	public List<LivroDTO> buscarPorAutorId(@RequestHeader(value = "autorId") Long autorId,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return livroService.buscarPorAutorId(autorId);
	}
}
