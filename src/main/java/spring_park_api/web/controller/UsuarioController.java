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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import spring_park_api.entity.Usuario;
import spring_park_api.service.UsuarioService;
import spring_park_api.web.dto.UsuarioCreateDTO;
import spring_park_api.web.dto.UsuarioResponseDTO;
import spring_park_api.web.dto.UsuarioSenhaDto;
import spring_park_api.web.dto.mapper.UsuarioMapper;
import spring_park_api.web.exception.ErrorMessage;

@Tag(name = "Usuarios", description = "Coném todas as operações relativas aos recursos para cadastro, edição e leitura de um usuario")
@RestController
@RequestMapping(value = "api/v1/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Operation(summary = "Criar um novo usuario", description = "Recurso oara criar novo usuario",
			responses = {
				@ApiResponse(responseCode = "201", description = "Recurso criado com sucesso", 
						content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioCreateDTO.class))),
				@ApiResponse(responseCode = "409", description = "Usuario email ja cadastrado no sistema", 
						content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
				@ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos", 
						content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
				})
	@PostMapping
	public ResponseEntity<UsuarioResponseDTO> create(@Valid @RequestBody UsuarioCreateDTO creatDto){
		Usuario user = usuarioService.criar(UsuarioMapper.toUsuario(creatDto));
		return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UsuarioResponseDTO> readById(@PathVariable Long id){
		Usuario user = usuarioService.buscarPorId(id);
		return ResponseEntity.ok(UsuarioMapper.toDto(user));
	}
	
	@PatchMapping(value = "/{id}")
	public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UsuarioSenhaDto dto){
		usuarioService.atualizarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	public ResponseEntity<List<UsuarioResponseDTO>> readAll(){
		List<Usuario> users = usuarioService.buscarTodos();
		return ResponseEntity.ok(UsuarioMapper.toListDto(users));
	}
}
