package spring_park_api.web.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import spring_park_api.entity.ClienteVaga;
import spring_park_api.repository.projection.ClienteVagaProjection;
import spring_park_api.service.ClienteVagaService;
import spring_park_api.service.EstacionamentoService;
import spring_park_api.web.dto.EstacionamentoCreateDto;
import spring_park_api.web.dto.EstacionamentoResponseDto;
import spring_park_api.web.dto.PageableDto;
import spring_park_api.web.dto.mapper.ClienteVagaMapper;
import spring_park_api.web.dto.mapper.PageableMapper;
import spring_park_api.web.exception.ErrorMessage;

@Tag(name = "Estacionamentos", description = "Operações de registro de entrada e saida de um veículo do estacionamento.")
@RestController
@RequestMapping("api/v1/estacionamentos")
public class EstacionamentoController {

	@Autowired
	private EstacionamentoService estacionamentoService;
	
	@Autowired
	private ClienteVagaService clienteVagaService;
	
	@Operation(summary = "Operação de chek-in", description = "Recurso para dar entrada de um veiculo no estacionamento. " +
			"Requisição exige um bearer token. Acesso restrito a Role='ADMIN'",
			security = @SecurityRequirement(name = "security"),
			responses = {
					@ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
							headers = @Header(name = HttpHeaders.LOCATION, description = "URL de acesso ao recurso criado"),
							content = @Content(mediaType = "application/json;charset=UTF-8",
										schema = @Schema(implementation = EstacionamentoResponseDto.class))),
					@ApiResponse(responseCode = "404", description = "Causas possiveis: <br/>"+
										"- CPF do cliente não cadastrado no sistema; <br/>" +
										"- Nenhuma vaga livre foi localizada;",
							content = @Content(mediaType = "application/json;charset=UTF-8",
									schema = @Schema(implementation = ErrorMessage.class))),
					@ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados invalidos",
							content = @Content(mediaType = "application/json;charset=UTF-8",
									schema = @Schema(implementation = ErrorMessage.class))),
					@ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
							content = @Content(mediaType = "application/json;charset=UTF-8",
									schema = @Schema(implementation = ErrorMessage.class)))
			})
	@PostMapping("/checkin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<EstacionamentoResponseDto> checkin(@RequestBody @Valid EstacionamentoCreateDto dto){
		ClienteVaga clienteVaga = ClienteVagaMapper.toClienteVaga(dto);
		estacionamentoService.checkIn(clienteVaga);
		EstacionamentoResponseDto responseDto = ClienteVagaMapper.toDto(clienteVaga);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequestUri().path("/{recibo}")
				.buildAndExpand(clienteVaga.getRecibo())
				.toUri();
				
		return ResponseEntity.created(location).body(responseDto);
	}
	
	@Operation(summary = "Localizar um veículo estacionado", description = "Recurso para retornar um veículo estacionado " +
			"pelo numero do recibo. Requisição exige um bearer token.'",
			security = @SecurityRequirement(name = "security"),
			parameters = {
					@Parameter(in = ParameterIn.PATH, name = "recibo", description = "Número do recibo gerado pelo checkin")
			},
			responses = {
					@ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
							content = @Content(mediaType = "application/json;charset=UTF-8",
									schema = @Schema(implementation = EstacionamentoResponseDto.class))),
					@ApiResponse(responseCode = "404", description = "Número do recibo não encontrado", 
							content = @Content(mediaType = "application/json;charset=UTF-8",
									schema = @Schema(implementation = ErrorMessage.class))),
			})
	@GetMapping("/checkin/{recibo}")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')" )
	public ResponseEntity<EstacionamentoResponseDto> getByRecibo(@PathVariable String recibo){
		ClienteVaga clienteVaga = clienteVagaService.buscarPorRecibo(recibo);
		EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
		return ResponseEntity.ok(dto);
	}
	
	@Operation(summary = "Operação de checkout", description = "Recurso para dar saida de um veículo estacionado " +
			"pelo numero do recibo. Requisição exige um bearer token. Acesso restrito a Role='ADMIN''",
			security = @SecurityRequirement(name = "security"),
			parameters = {
					@Parameter(in = ParameterIn.PATH, name = "recibo", description = "Número do recibo gerado pelo checkin")
			},
			responses = {
					@ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
							content = @Content(mediaType = "application/json;charset=UTF-8",
									schema = @Schema(implementation = EstacionamentoResponseDto.class))),
					@ApiResponse(responseCode = "404", description = "Número do recibo inexistente ou " +
									"o veículo ja passou pelo checkout.", 
							content = @Content(mediaType = "application/json;charset=UTF-8",
									schema = @Schema(implementation = ErrorMessage.class))),
					@ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
							content = @Content(mediaType = "application/json;charset=UTF-8",
									schema = @Schema(implementation = ErrorMessage.class)))
			})
	@PutMapping("/checkout/{recibo}")
	@PreAuthorize("hasRole('ADMIN')" )
	public ResponseEntity<EstacionamentoResponseDto> checkout(@PathVariable String recibo){
		ClienteVaga clienteVaga = estacionamentoService.checkout(recibo);
		EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
		return ResponseEntity.ok(dto);
	}
	
	@Operation(summary = "Localizar os registros de estacionamentos do cliente por cpf", description = "Localizar os  " +
			"registros de estacionamentos do cliente por CPF. Requisição exige uso de um bearer token.''",
			security = @SecurityRequirement(name = "security"),
			parameters = {
					@Parameter(in = ParameterIn.PATH, name = "cpf", description = "Número do cpf referente ao cliente a ser consultado", required = true
							),
					@Parameter(in = ParameterIn.QUERY, name = "page", description = "Representa a pagina retornada",
									content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))
							),
					@Parameter(in = ParameterIn.QUERY, name = "size", description = "Representa o total de elementos por pagina",
									content = @Content(schema = @Schema(type = "integer", defaultValue = "5"))
							),
					@Parameter(in = ParameterIn.QUERY, name = "sort", description = "Campo padrão de ordenação 'dataEntrada,asc'. ",
									array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dataEntrada,asc")),
									hidden = true
							)
			},
			responses = {
					@ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
							content = @Content(mediaType = "application/json;charset=UTF-8",
									schema = @Schema(implementation = PageableDto.class))),
					@ApiResponse(responseCode = "403", description = "Rescurso não permitido ao perfil de CLIENTE", 
							content = @Content(mediaType = "application/json;charset=UTF-8",
									schema = @Schema(implementation = ErrorMessage.class))),
			})
	@GetMapping("/cpf/{cpf}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PageableDto> getAllEstacionamentosPorCpf(@PathVariable String cpf, 
																															 @PageableDefault(size = 5, sort = "dataEntrada", direction = Sort.Direction.ASC ) Pageable pageable){
		Page<ClienteVagaProjection> projection = clienteVagaService.buscarTodosPorClienteCpf(cpf, pageable);
		PageableDto dto = PageableMapper.toDto(projection);
		return ResponseEntity.ok(dto);
	}
}
