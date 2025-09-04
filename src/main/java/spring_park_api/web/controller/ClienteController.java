package spring_park_api.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import spring_park_api.entity.Cliente;
import spring_park_api.jwt.JwtUserDetails;
import spring_park_api.service.ClienteService;
import spring_park_api.service.UsuarioService;
import spring_park_api.web.dto.ClienteCreateDto;
import spring_park_api.web.dto.ClienteResponseDto;
import spring_park_api.web.dto.mapper.ClienteMapper;

@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto, @AuthenticationPrincipal JwtUserDetails userDetails){
		Cliente cliente = ClienteMapper.toCliente(dto);
		cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
		clienteService.criar(cliente);
		return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
	}
}
