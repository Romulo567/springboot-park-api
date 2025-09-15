package spring_park_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import spring_park_api.web.dto.EstacionamentoCreateDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/estacionamentos/estacionamentos-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/estacionamentos/estacionamentos-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class EstacionamentosIT {

	@Autowired
	WebTestClient testCliente;
	
	@Test
	public void criarChekin_ComDadosValidos_RetornarCreatedAndLocation() {
		EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
						.placa("WER-1111").marca("FIAT").modelo("PALIO 1.0")
						.cor("AZUL").clienteCpf("98133219035")
						.build();
		
		testCliente
						.post()
						.uri("/api/v1/estacionamentos/checkin")
						.contentType(MediaType.APPLICATION_JSON)
						.headers(JwtAuthentication.getHeaderAuthorization(testCliente, "ana@gmail.com", "123456" ))
						.bodyValue(createDto)
						.exchange()
						.expectStatus().isCreated()
						.expectHeader().exists(HttpHeaders.LOCATION)
						.expectBody()
						.jsonPath("placa").isEqualTo("WER-1111")
						.jsonPath("marca").isEqualTo("FIAT")
						.jsonPath("modelo").isEqualTo("PALIO 1.0")
						.jsonPath("cor").isEqualTo("AZUL")
						.jsonPath("clienteCpf").isEqualTo("98133219035")
						.jsonPath("recibo").exists()
						.jsonPath("dataEntrada").exists()
						.jsonPath("vagaCodigo").exists();
	}
	
	@Test
	public void criarChekin_ComRoleCliente_RetornarErrorStatus403() {
		EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
						.placa("WER-1111").marca("FIAT").modelo("PALIO 1.0")
						.cor("AZUL").clienteCpf("98133219035")
						.build();
		
		testCliente
						.post()
						.uri("/api/v1/estacionamentos/checkin")
						.contentType(MediaType.APPLICATION_JSON)
						.headers(JwtAuthentication.getHeaderAuthorization(testCliente, "kelvin@gmail.com", "123456" ))
						.bodyValue(createDto)
						.exchange()
						.expectStatus().isForbidden()
						.expectBody()
						.jsonPath("status").isEqualTo("403")
						.jsonPath("path").isEqualTo("/api/v1/estacionamentos/checkin")
						.jsonPath("method").isEqualTo("POST");
	}
	
	@Test
	public void criarChekin_ComDadosInvalids_RetornarErrorStatus422() {
		EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
						.placa("").marca("").modelo("")
						.cor("").clienteCpf("")
						.build();
		
		testCliente
						.post()
						.uri("/api/v1/estacionamentos/checkin")
						.contentType(MediaType.APPLICATION_JSON)
						.headers(JwtAuthentication.getHeaderAuthorization(testCliente, "kelvin@gmail.com", "123456" ))
						.bodyValue(createDto)
						.exchange()
						.expectStatus().isEqualTo(422)
						.expectBody()
						.jsonPath("status").isEqualTo("422")
						.jsonPath("path").isEqualTo("/api/v1/estacionamentos/checkin")
						.jsonPath("method").isEqualTo("POST");
	}
}
