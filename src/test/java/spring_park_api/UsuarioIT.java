package spring_park_api;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import spring_park_api.web.dto.UsuarioCreateDTO;
import spring_park_api.web.dto.UsuarioResponseDTO;
import spring_park_api.web.dto.UsuarioSenhaDto;
import spring_park_api.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

	@Autowired
	WebTestClient testClient;
	
	@Test
	public void createUsuario_ComUsernameEPasswordValidos_RetornarUsuarioCriadoComStatus201() {
		UsuarioResponseDTO responseBody = testClient
			.post()
			.uri("/api/v1/usuarios")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(new UsuarioCreateDTO("eduarda@gmail.com", "123456"))
			.exchange()
			.expectStatus().isCreated()
			.expectBody(UsuarioResponseDTO.class)
			.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("eduarda@gmail.com");
		org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
		
	}
	
	@Test
	public void createUsuario_ComUsernameInvalido_RetornarErrorMessageStatus422() {
		ErrorMessage responseBody = testClient
			.post()
			.uri("/api/v1/usuarios")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(new UsuarioCreateDTO("", "123456"))
			.exchange()
			.expectStatus().isEqualTo(422)
			.expectBody(ErrorMessage.class)
			.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
		
		responseBody = testClient
				.post()
				.uri("/api/v1/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioCreateDTO("eduarda@", "123456"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();
			
			org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
			org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
			
			responseBody = testClient
					.post()
					.uri("/api/v1/usuarios")
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(new UsuarioCreateDTO("eduarda@gmail", "123456"))
					.exchange()
					.expectStatus().isEqualTo(422)
					.expectBody(ErrorMessage.class)
					.returnResult().getResponseBody();
				
				org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
				org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
	}
	
	@Test
	public void createUsuario_ComPasswordInvalido_RetornarErrorMessageStatus422() {
		ErrorMessage responseBody = testClient
			.post()
			.uri("/api/v1/usuarios")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(new UsuarioCreateDTO("eduarda@gmail.com", ""))
			.exchange()
			.expectStatus().isEqualTo(422)
			.expectBody(ErrorMessage.class)
			.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
		
		responseBody = testClient
				.post()
				.uri("/api/v1/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioCreateDTO("eduarda@gmail.com", "123"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();
			
			org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
			org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
			
			responseBody = testClient
					.post()
					.uri("/api/v1/usuarios")
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(new UsuarioCreateDTO("eduarda@gmail.com", "123456789"))
					.exchange()
					.expectStatus().isEqualTo(422)
					.expectBody(ErrorMessage.class)
					.returnResult().getResponseBody();
				
				org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
				org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
	}
	
	@Test
	public void createUsuario_ComUsernameRepetido_RetornarErrorMessageComStatus2409() {
		ErrorMessage responseBody = testClient
			.post()
			.uri("/api/v1/usuarios")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(new UsuarioCreateDTO("ana@gmail.com", "123456"))
			.exchange()
			.expectStatus().isEqualTo(409)
			.expectBody(ErrorMessage.class)
			.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
		
	}
	
	@Test
	public void buscarUsuario_ComIdExistente_RetornarUsuarioComStatus200() {
		UsuarioResponseDTO responseBody = testClient
			.get()
			.uri("/api/v1/usuarios/100")
			.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
			.exchange()
			.expectStatus().isOk()
			.expectBody(UsuarioResponseDTO.class)
			.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(100);
		org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@gmail.com");
		org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");
		
		responseBody = testClient
				.get()
				.uri("/api/v1/usuarios/101")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
				.exchange()
				.expectStatus().isOk()
				.expectBody(UsuarioResponseDTO.class)
				.returnResult().getResponseBody();

		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(101);
		org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("kelvin@gmail.com");
		org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
		
		responseBody = testClient
				.get()
				.uri("/api/v1/usuarios/101")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "kelvin@gmail.com", "123456"))
				.exchange()
				.expectStatus().isOk()
				.expectBody(UsuarioResponseDTO.class)
				.returnResult().getResponseBody();

		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(101);
		org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("kelvin@gmail.com");
		org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
	}
	
	@Test
	public void buscarUsuario_ComIdInexistente_RetornarErrorMessageComStatus404() {
		ErrorMessage responseBody = testClient
			.get()
			.uri("/api/v1/usuarios/0")
			.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
			.exchange()
			.expectStatus().isNotFound()
			.expectBody(ErrorMessage.class)
			.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
	}
	
	@Test
	public void buscarUsuario_ComUsuarioClienteBuscandoOutroCliente_RetornarErrorMessageComStatus403() {
		ErrorMessage responseBody = testClient
			.get()
			.uri("/api/v1/usuarios/102")
			.headers(JwtAuthentication.getHeaderAuthorization(testClient, "kelvin@gmail.com", "123456"))
			.exchange()
			.expectStatus().isForbidden()
			.expectBody(ErrorMessage.class)
			.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
	}
	
	@Test
	public void editarSenha_ComDadosdValidos_RetornarStatus204() {
		 testClient
				.patch()
				.uri("/api/v1/usuarios/100")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioSenhaDto("123456", "123456", "123456"))
				.exchange()
				.expectStatus()
				.isNoContent();
		 
		 testClient
				.patch()
				.uri("/api/v1/usuarios/101")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "kelvin@gmail.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioSenhaDto("123456", "123456", "123456"))
				.exchange()
				.expectStatus()
				.isNoContent();
	}
	
	@Test
	public void editarSenha_ComUsuariosDiferentes_RetornarErrorMessageComStatus403() {
		ErrorMessage responseBody = testClient
				.patch()
				.uri("/api/v1/usuarios/0")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioSenhaDto("123456", "123456", "123456"))
				.exchange()
				.expectStatus().isForbidden()
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
		
		responseBody = testClient
					.patch()
					.uri("/api/v1/usuarios/0")
					.headers(JwtAuthentication.getHeaderAuthorization(testClient, "kelvin@gmail.com", "123456"))
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(new UsuarioSenhaDto("123456", "123456", "123456"))
					.exchange()
					.expectStatus().isForbidden()
					.expectBody(ErrorMessage.class)
					.returnResult().getResponseBody();
			
			org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
			org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
	}
	
	@Test
	public void editarSenha_ComCamposInvalidos_RetornarErrorMessageComStatus422() {
		ErrorMessage responseBody = testClient
			.patch()
			.uri("/api/v1/usuarios/100")
			.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(new UsuarioSenhaDto("", "", ""))
			.exchange()
			.expectStatus().isEqualTo(422)
			.expectBody(ErrorMessage.class)
			.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
		
		responseBody = testClient
				.patch()
				.uri("/api/v1/usuarios/100")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioSenhaDto("12345", "12345", "12345"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();
			
			org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
			org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
			
			responseBody = testClient
					.patch()
					.uri("/api/v1/usuarios/100")
					.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(new UsuarioSenhaDto("12345678", "12345678", ""))
					.exchange()
					.expectStatus().isEqualTo(422)
					.expectBody(ErrorMessage.class)
					.returnResult().getResponseBody();
				
				org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
				org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
	}
	
	@Test
	public void editarSenha_ComSenhasInvalidos_RetornarErrorMessageComStatus400() {
		ErrorMessage responseBody = testClient
			.patch()
			.uri("/api/v1/usuarios/100")
			.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(new UsuarioSenhaDto("123456", "123456", "000000"))
			.exchange()
			.expectStatus().isEqualTo(400)
			.expectBody(ErrorMessage.class)
			.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
		
		responseBody = testClient
				.patch()
				.uri("/api/v1/usuarios/100")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioSenhaDto("000000", "123456", "123456"))
				.exchange()
				.expectStatus().isEqualTo(400)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();
			
			org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
			org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
	}
	
	@Test
	public void listarUsuarios_SemQualquerParametro_RetornarListaDeUsuariosComStatus200() {
		List<UsuarioResponseDTO> responseBody = testClient
				.get()
				.uri("/api/v1/usuarios")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(UsuarioResponseDTO.class)
				.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.size()).isEqualTo(3);
	}
}
