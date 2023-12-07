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
import br.upf.usuariolivroautor.dto.UsuarioDTO;
import br.upf.usuariolivroautor.service.UsuarioService;
import br.upf.usuariolivroautor.utils.TokenJWT;

@RestController
@RequestMapping(value = "/usuariolivroautor/usuario")
public class UsuarioController {

	@Autowired // mecanismo de injeção de dependência
	private UsuarioService usuarioService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping(value = "/inserir")
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioDTO inserir(@RequestBody UsuarioDTO userDTO,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return usuarioService.salvar(userDTO);
	}
	
	@GetMapping(value = "/listarTodos")
	@ResponseStatus(HttpStatus.OK)
	public List<UsuarioDTO> listarTodos(@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token); 
		return usuarioService.listarTodos();
	}
	
	@GetMapping(value = "/buscarPorId")
	@ResponseStatus(HttpStatus.OK)
	public UsuarioDTO buscarPorId(@RequestHeader(value = "id") Long id,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token); 
		return usuarioService.buscarPorId(id)
				// caso o usario n foi encontrado...
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Usuário não encontrado."));
	}
	
	@DeleteMapping(value = "/delete")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerUsuario(@RequestHeader(value = "id") Long id,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		usuarioService.buscarPorId(id) //antes de deletar, busca na base o usuario...
			.map(usuario -> { // caso encontre o usuario, remova o mesmo
				usuarioService.removerPorId(usuario.getId()); 
				return Void.TYPE;
				// caso n encontre, retorna o status
			}).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Usuário não encontrado."));			
	}
	
	/** 
	 * Para implementar o atualizar, é necessario incluir o
	 * metodo bean modelMapper() na classe ...Apllication.java
	 * @param user
	 * @param id
	 */
	@PutMapping(value = "/atualizar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizar(@RequestBody UsuarioDTO user,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		usuarioService.buscarPorId(user.getId()).map(usuarioBase -> {
			//recurso do modelMap que verifica o que esta no parametro para
			//atualizar na base.
			//esse recurso necessita de incluir a dependencia modelMapper no pom.xml
			modelMapper.map(user, usuarioBase);
			usuarioService.salvar(usuarioBase); 
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
				"Usuário não encontrado."));		
	}
	
	@GetMapping(value = "/buscarPorEmail")
	@ResponseStatus(HttpStatus.OK)
	public UsuarioDTO findByEmail(@RequestHeader(value = "email") String email,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return usuarioService.buscarPorEmail(email);
	}
	
	@GetMapping(value = "/buscarPorParteNome")
	@ResponseStatus(HttpStatus.OK)
	public List<UsuarioDTO> buscarPorParteNome(@RequestHeader(value = "nome") String nome,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return usuarioService.buscarPorParteNome(nome);
	}
	
	@GetMapping(value = "/buscarPorSenha")
	@ResponseStatus(HttpStatus.OK)
	public List<UsuarioDTO> buscarPorSenha(@RequestHeader(value = "senha") String senha,
			@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return usuarioService.buscarPorSenha(senha);
	}
	
	@GetMapping(value = "/authorize")
	@ResponseStatus(HttpStatus.OK)
	public UsuarioDTO authorize(@RequestHeader(value = "email") String email,
			@RequestHeader(value = "password") String password) {
		UsuarioDTO userDTO;
		// validando se usuario e senha foram enviados corretamente...
		if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
			userDTO = usuarioService.buscarPorEmail(email);
			// se encontrar o usuario no banco...
			if (userDTO.getId() != null) {
				//se todas as credenciais foram validas...
				if (userDTO.getSenha().equals(password)) {
					//adicionar o token no retorno da entidade...
					userDTO.setToken(TokenJWT.processarTokenJWT(email));
					return userDTO;
				} else { // se a senha n estiver correta...
					return null;
				}
			} else { // se n encontrar usuario no banco...
				return null;
			}
		} else { // se n enviou usuario e senha corretamente...
			return null;
		}
	}
}