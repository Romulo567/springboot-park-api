package spring_park_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import spring_park_api.web.dto.ClienteCreateDto;
import spring_park_api.web.dto.ClienteResponseDto;
import spring_park_api.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clientes/clientes-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClienteIT {

	@Autowired
	WebTestClient testeClient;
	
	@Test
	public void criarCliente_ComDadosValidos_RetornarClienteComStatus201() {
		ClienteResponseDto responseBody = testeClient
						.post()
						.uri("/api/v1/clientes")
						.contentType(MediaType.APPLICATION_JSON)
						.headers(JwtAuthentication.getHeaderAuthorization(testeClient, "toby@gmail.com", "123456"))
						.bodyValue(new ClienteCreateDto("Tobias Ferreira", "55746061000"))
						.exchange()
						.expectStatus().isCreated()
						.expectBody(ClienteResponseDto.class)
						.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Tobias Ferreira");
		org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("55746061000");			
	}
	
	@Test
	public void criarCliente_ComCpfJaCadastrado_RetornarErrorMessageComStatus409() {
		ErrorMessage responseBody = testeClient
						.post()
						.uri("/api/v1/clientes")
						.contentType(MediaType.APPLICATION_JSON)
						.headers(JwtAuthentication.getHeaderAuthorization(testeClient, "toby@gmail.com", "123456"))
						.bodyValue(new ClienteCreateDto("Tobias Ferreira", "99012561000"))
						.exchange()
						.expectStatus().isEqualTo(409)
						.expectBody(ErrorMessage.class)
						.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
	}
	
	@Test
	public void criarCliente_ComDadosInvalidos_RetornarErrorMessageComStatus422() {
		ErrorMessage responseBody = testeClient
						.post()
						.uri("/api/v1/clientes")
						.contentType(MediaType.APPLICATION_JSON)
						.headers(JwtAuthentication.getHeaderAuthorization(testeClient, "toby@gmail.com", "123456"))
						.bodyValue(new ClienteCreateDto("", ""))
						.exchange()
						.expectStatus().isEqualTo(422)
						.expectBody(ErrorMessage.class)
						.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
		
		 responseBody = testeClient
						.post()
						.uri("/api/v1/clientes")
						.contentType(MediaType.APPLICATION_JSON)
						.headers(JwtAuthentication.getHeaderAuthorization(testeClient, "toby@gmail.com", "123456"))
						.bodyValue(new ClienteCreateDto("toby", "00000000000"))
						.exchange()
						.expectStatus().isEqualTo(422)
						.expectBody(ErrorMessage.class)
						.returnResult().getResponseBody();
	
			org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
			org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
			
			responseBody = testeClient
						.post()
						.uri("/api/v1/clientes")
						.contentType(MediaType.APPLICATION_JSON)
						.headers(JwtAuthentication.getHeaderAuthorization(testeClient, "toby@gmail.com", "123456"))
						.bodyValue(new ClienteCreateDto("toby", "557.460.610-00"))
						.exchange()
						.expectStatus().isEqualTo(422)
						.expectBody(ErrorMessage.class)
						.returnResult().getResponseBody();
	
			org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
			org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
	}
	
	@Test
	public void criarCliente_ComUsuarioNaoPermitido_RetornarErrorMessageComStatus403() {
		ErrorMessage responseBody = testeClient
						.post()
						.uri("/api/v1/clientes")
						.contentType(MediaType.APPLICATION_JSON)
						.headers(JwtAuthentication.getHeaderAuthorization(testeClient, "ana@gmail.com", "123456"))
						.bodyValue(new ClienteCreateDto("Tobias Ferreira", "55746061000"))
						.exchange()
						.expectStatus().isForbidden()
						.expectBody(ErrorMessage.class)
						.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
	}
	
	@Test
	public void buscarCliente_ComIdExistentePeloAdmin_RetornarClienteComStatus200() {
		ClienteResponseDto responseBody = testeClient
						.get()
						.uri("/api/v1/clientes/10")
						.headers(JwtAuthentication.getHeaderAuthorization(testeClient, "ana@gmail.com", "123456"))
						.exchange()
						.expectStatus().isOk()
						.expectBody(ClienteResponseDto.class)
						.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(10);
	}
	
	@Test
	public void buscarCliente_ComIdInexistentePeloAdmin_RetornarErrorMessageComStatus404() {
		ErrorMessage responseBody = testeClient
						.get()
						.uri("/api/v1/clientes/0")
						.headers(JwtAuthentication.getHeaderAuthorization(testeClient, "ana@gmail.com", "123456"))
						.exchange()
						.expectStatus().isNotFound()
						.expectBody(ErrorMessage.class)
						.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
	}
	
	@Test
	public void buscarCliente_ComIdExistentePeloCliente_RetornarErrorMessageComStatus403() {
		ErrorMessage responseBody = testeClient
						.get()
						.uri("/api/v1/clientes/0")
						.headers(JwtAuthentication.getHeaderAuthorization(testeClient, "kelvin@gmail.com", "123456"))
						.exchange()
						.expectStatus().isForbidden()
						.expectBody(ErrorMessage.class)
						.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
	}
}
