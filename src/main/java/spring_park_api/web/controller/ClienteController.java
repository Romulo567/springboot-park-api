package spring_park_api.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
import spring_park_api.entity.Cliente;
import spring_park_api.jwt.JwtUserDetails;
import spring_park_api.service.ClienteService;
import spring_park_api.service.UsuarioService;
import spring_park_api.web.dto.ClienteCreateDto;
import spring_park_api.web.dto.ClienteResponseDto;
import spring_park_api.web.dto.mapper.ClienteMapper;
import spring_park_api.web.exception.ErrorMessage;

@Tag(name = "Clientes", description =  "Contém todas as operações relativas ao recurso de um cliente")
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Operation(summary = "Criar um novo cliente", description = "Recurso para criar novo cliente vinculado a um usuario cadastrado. " +
						"Requisição exige uso de um Bearer token. Acesso restrito a Role='CLIENTE'",
			responses = {
				@ApiResponse(responseCode = "201", description = "Recurso criado com sucesso", 
						content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),
				@ApiResponse(responseCode = "409", description = "Cliente CPF ja possui cadastro no sistema", 
						content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
				@ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos", 
						content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
				@ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de ADMIN", 
						content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
				})
	@PostMapping
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto, @AuthenticationPrincipal JwtUserDetails userDetails){
		Cliente cliente = ClienteMapper.toCliente(dto);
		cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
		clienteService.criar(cliente);
		return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
	}
	
	@Operation(summary = "Localizar um cliente", description = "Recurso para localizar um cliente por id " +
			"Requisição exige uso de um Bearer token. Acesso restrito a Role='ADMIN'",
			responses = {
				@ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso", 
						content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),
				@ApiResponse(responseCode = "404", description = "Cliente não encontrado", 
						content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
				@ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE", 
						content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
				})
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ClienteResponseDto> readById(@PathVariable Long id){
		Cliente cliente = clienteService.buscarPorId(id);
		return ResponseEntity.ok(ClienteMapper.toDto(cliente));
	}
	
	@GetMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<Cliente>> readAll(Pageable pageable){
		Page<Cliente> clientes = clienteService.buscarTodos(pageable);
		return ResponseEntity.ok(clientes);
	}
	
}
