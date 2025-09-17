package spring_park_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import spring_park_api.web.dto.EstacionamentoCreateDto;
import spring_park_api.web.dto.PageableDto;

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
	
	@Test
	public void criarChekin_ComCpfInexistente_RetornarErrorStatus404() {
		EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
				.placa("WER-1111").marca("FIAT").modelo("PALIO 1.0")
				.cor("AZUL").clienteCpf("46113540081")
				.build();
		
		testCliente
						.post()
						.uri("/api/v1/estacionamentos/checkin")
						.contentType(MediaType.APPLICATION_JSON)
						.headers(JwtAuthentication.getHeaderAuthorization(testCliente, "ana@gmail.com", "123456" ))
						.bodyValue(createDto)
						.exchange()
						.expectStatus().isNotFound()
						.expectBody()
						.jsonPath("status").isEqualTo("404")
						.jsonPath("path").isEqualTo("/api/v1/estacionamentos/checkin")
						.jsonPath("method").isEqualTo("POST");
	}
	
	@Sql(scripts = "/sql/estacionamentos/estacionamentos-insert-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/estacionamentos/estacionamentos-delete-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void criarChekin_ComVagasOcupadas_RetornarErrorStatus404() {
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
						.expectStatus().isNotFound()
						.expectBody()
						.jsonPath("status").isEqualTo("404")
						.jsonPath("path").isEqualTo("/api/v1/estacionamentos/checkin")
						.jsonPath("method").isEqualTo("POST");
	}
	
	@Test
	public void buscarChekin_ComPerfilAdmin_RetornarDadosComStatus200() {

		testCliente
						.get()
						.uri("/api/v1/estacionamentos/checkin/{recibo}", "20250313-101300")
						.headers(JwtAuthentication.getHeaderAuthorization(testCliente, "ana@gmail.com", "123456" ))
						.exchange()
						.expectStatus().isOk()
						.expectBody()
						.jsonPath("placa").isEqualTo("FIT-1020")
						.jsonPath("marca").isEqualTo("FIAT")
						.jsonPath("modelo").isEqualTo("PALIO")
						.jsonPath("cor").isEqualTo("VERDE")
						.jsonPath("clienteCpf").isEqualTo("99012561000")
						.jsonPath("recibo").isEqualTo("20250313-101300")
						.jsonPath("dataEntrada").isEqualTo("2025-03-13 10:15:00")
						.jsonPath("vagaCodigo").isEqualTo("A-01");
	}
	
	@Test
	public void buscarChekin_ComPerfilCliente_RetornarDadosComStatus200() {

		testCliente
						.get()
						.uri("/api/v1/estacionamentos/checkin/{recibo}", "20250313-101300")
						.headers(JwtAuthentication.getHeaderAuthorization(testCliente, "vitor@gmail.com", "123456" ))
						.exchange()
						.expectStatus().isOk()
						.expectBody()
						.jsonPath("placa").isEqualTo("FIT-1020")
						.jsonPath("marca").isEqualTo("FIAT")
						.jsonPath("modelo").isEqualTo("PALIO")
						.jsonPath("cor").isEqualTo("VERDE")
						.jsonPath("clienteCpf").isEqualTo("99012561000")
						.jsonPath("recibo").isEqualTo("20250313-101300")
						.jsonPath("dataEntrada").isEqualTo("2025-03-13 10:15:00")
						.jsonPath("vagaCodigo").isEqualTo("A-01");
	}
	
	@Test
	public void buscarChekin_ComReciboInexistente_RetornarErrorComStatus404() {

		testCliente
						.get()
						.uri("/api/v1/estacionamentos/checkin/{recibo}", "20250313-999999")
						.headers(JwtAuthentication.getHeaderAuthorization(testCliente, "vitor@gmail.com", "123456" ))
						.exchange()
						.expectStatus().isNotFound()
						.expectBody()
						.jsonPath("status").isEqualTo("404")
						.jsonPath("path").isEqualTo("/api/v1/estacionamentos/checkin/20250313-999999")
						.jsonPath("method").isEqualTo("GET");
	}
	
	@Test
	public void criarCheckout_ComReciboExistente_RetornarSucesso() {

		testCliente
						.put()
						.uri("/api/v1/estacionamentos/checkout/{recibo}", "20250313-101300")
						.headers(JwtAuthentication.getHeaderAuthorization(testCliente, "ana@gmail.com", "123456" ))
						.exchange()
						.expectStatus().isOk()
						.expectBody()
						.jsonPath("placa").isEqualTo("FIT-1020")
						.jsonPath("marca").isEqualTo("FIAT")
						.jsonPath("modelo").isEqualTo("PALIO")
						.jsonPath("cor").isEqualTo("VERDE")
						.jsonPath("dataEntrada").isEqualTo("2025-03-13 10:15:00")
						.jsonPath("clienteCpf").isEqualTo("99012561000")
						.jsonPath("vagaCodigo").isEqualTo("A-01")
						.jsonPath("recibo").isEqualTo("20250313-101300")
						.jsonPath("dataSaida").exists()
						.jsonPath("valor").exists()
						.jsonPath("desconto").exists();
	}
	
	@Test
	public void criarCheckout_ComReciboInexistente_RetornarErrorComStatus404() {

		testCliente
						.put()
						.uri("/api/v1/estacionamentos/checkout/{recibo}", "20250313-000000")
						.headers(JwtAuthentication.getHeaderAuthorization(testCliente, "ana@gmail.com", "123456" ))
						.exchange()
						.expectStatus().isNotFound()
						.expectBody()
						.jsonPath("status").isEqualTo("404")
						.jsonPath("path").isEqualTo("/api/v1/estacionamentos/checkout/20250313-000000")
						.jsonPath("method").isEqualTo("PUT");
	}
	
	@Test
	public void criarCheckout_ComRoleCliente_RetornarErrorComStatus403() {

		testCliente
						.put()
						.uri("/api/v1/estacionamentos/checkout/{recibo}", "20250313-101300")
						.headers(JwtAuthentication.getHeaderAuthorization(testCliente, "kelvin@gmail.com", "123456" ))
						.exchange()
						.expectStatus().isForbidden()
						.expectBody()
						.jsonPath("status").isEqualTo("403")
						.jsonPath("path").isEqualTo("/api/v1/estacionamentos/checkout/20250313-101300")
						.jsonPath("method").isEqualTo("PUT");
	}
	
	@Test
	public void buscarEstacionamentos_PorClienteCpf_RetornarSucesso() {

		PageableDto responseBody = testCliente
						.get()
						.uri("/api/v1/estacionamentos/cpf/{cpf}?size=1&page=0", "99012561000")
						.headers(JwtAuthentication.getHeaderAuthorization(testCliente, "ana@gmail.com", "123456" ))
						.exchange()
						.expectStatus().isOk()
						.expectBody(PageableDto.class)
						.returnResult().getResponseBody();
		
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
		org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
		org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
		org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
		
		responseBody = testCliente
					.get()
					.uri("/api/v1/estacionamentos/cpf/{cpf}?size=1&page=1", "99012561000")
					.headers(JwtAuthentication.getHeaderAuthorization(testCliente, "ana@gmail.com", "123456" ))
					.exchange()
					.expectStatus().isOk()
					.expectBody(PageableDto.class)
					.returnResult().getResponseBody();

		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
		org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
		org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
		org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
		}
	
	@Test
	public void buscarEstacionamentos_PorClienteCpfComPerfilCliente_RetornarErrorComStatus403() {

		testCliente
						.get()
						.uri("/api/v1/estacionamentos/cpf/{cpf}", "99012561000")
						.headers(JwtAuthentication.getHeaderAuthorization(testCliente, "kelvin@gmail.com", "123456" ))
						.exchange()
						.expectStatus().isForbidden()
						.expectBody()
						.jsonPath("status").isEqualTo("403")
						.jsonPath("path").isEqualTo("/api/v1/estacionamentos/cpf/99012561000")
						.jsonPath("method").isEqualTo("GET");
	}
}
