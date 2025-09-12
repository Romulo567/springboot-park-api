package spring_park_api.web.dto.mapper;

import org.modelmapper.ModelMapper;

import spring_park_api.entity.ClienteVaga;
import spring_park_api.web.dto.EstacionamentoCreateDto;
import spring_park_api.web.dto.EstacionamentoResponseDto;

public class ClienteVagaMapper {

	public static ClienteVaga toClienteVaga(EstacionamentoCreateDto dto) {
		return new ModelMapper().map(dto, ClienteVaga.class);
	}
	
	public static EstacionamentoResponseDto toDto(ClienteVaga clienteVaga) {
		return new ModelMapper().map(clienteVaga, EstacionamentoResponseDto.class);
	}
	
	private ClienteVagaMapper() {
	}
}
