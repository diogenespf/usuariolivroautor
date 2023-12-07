package br.upf.usuariolivroautor.controller;

import java.util.Date;
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
import br.upf.usuariolivroautor.dto.AutorDTO;
import br.upf.usuariolivroautor.service.AutorService;
import br.upf.usuariolivroautor.utils.TokenJWT;

@RestController
@RequestMapping(value = "/usuariolivroautor/autor")
public class AutorController {
	@Autowired
	private AutorService autorService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping(value = "/inserir")
	@ResponseStatus(HttpStatus.CREATED)
	public AutorDTO inserir(@RequestBody AutorDTO autorDTO, 
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token); 
		return autorService.salvar(autorDTO);
	}
	
	@GetMapping(value = "/listarTodos")
	@ResponseStatus(HttpStatus.OK)
	public List<AutorDTO> listarTodos(@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return autorService.listarTodos();
	}
	
	@GetMapping(value = "/buscarPorId")
	@ResponseStatus(HttpStatus.OK)
	public AutorDTO buscarPorId(@RequestHeader(value = "id") Long id,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return autorService.buscarPorId(id)
				// caso o autor n foi encontrado...
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Autor não encontrado."));
	}
	
	@DeleteMapping(value = "/delete")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerAutor(@RequestHeader(value = "id") Long id,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		autorService.buscarPorId(id) //antes de deletar, busca na base o autor...
			.map(autor -> { //definindo a variavel no map
				autorService.removerPorId(autor.getId()); // caso encontre o autor
				return Void.TYPE;
				// caso n encontre, retorna o status
			}).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Autor não encontrado."));			
	}
	
	/** 
	 * Para implementar o atualizar, é necessario incluir o
	 * metodo bean modelMapper() na classe ...Apllication.java
	 * @param autorDTO
	 * @param id
	 */
	@PutMapping(value = "/atualizar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizar(@RequestBody AutorDTO autorDTO,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		autorService.buscarPorId(autorDTO.getId()).map(autorBase -> {
			//recurso do modelMap que verifica o que esta no parametro para
			//atualizar na base.
			//esse recurso necessita de incluir a dependencia modelMapper no pom.xml
			modelMapper.map(autorDTO, autorBase);
			autorService.salvar(autorBase); //salvar os itens alterados
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
				"Autor não encontrado."));		
	}
	
	@GetMapping(value = "/buscarPorParteNome")
	@ResponseStatus(HttpStatus.OK)
	public List<AutorDTO> buscarPorParteNome(@RequestHeader(value = "nome") String nome,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return autorService.buscarPorParteNome(nome);
	}
	
	@GetMapping(value = "/buscarPorDataNascimento")
	@ResponseStatus(HttpStatus.OK)
	public List<AutorDTO> buscarPorDataNascimento(@RequestHeader(value = "dataNascimento") Date dataNascimento,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return autorService.buscarPorDataNascimento(dataNascimento);
	}
}
