package spring_park_api.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring_park_api.entity.Usuario;
import spring_park_api.service.UsuarioService;
import spring_park_api.web.dto.UsuarioCreateDTO;
import spring_park_api.web.dto.UsuarioResponseDTO;
import spring_park_api.web.dto.UsuarioSenhaDto;
import spring_park_api.web.dto.mapper.UsuarioMapper;

@RestController
@RequestMapping(value = "api/v1/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping
	public ResponseEntity<UsuarioResponseDTO> create(@RequestBody UsuarioCreateDTO creatDto){
		Usuario user = usuarioService.criar(UsuarioMapper.toUsuario(creatDto));
		return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UsuarioResponseDTO> readById(@PathVariable Long id){
		Usuario user = usuarioService.buscarPorId(id);
		return ResponseEntity.ok(UsuarioMapper.toDto(user));
	}
	
	@PatchMapping(value = "/{id}")
	public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UsuarioSenhaDto dto){
		usuarioService.atualizarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	public ResponseEntity<List<UsuarioResponseDTO>> readAll(){
		List<Usuario> users = usuarioService.buscarTodos();
		return ResponseEntity.ok(UsuarioMapper.toListDto(users));
	}
}
