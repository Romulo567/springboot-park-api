package spring_park_api.web.dto.mapper;

import org.modelmapper.ModelMapper;

import spring_park_api.entity.Cliente;
import spring_park_api.web.dto.ClienteCreateDto;
import spring_park_api.web.dto.ClienteResponseDto;

public class ClienteMapperDto {

	public static Cliente toCliente(ClienteCreateDto dto) {
		return new ModelMapper().map(dto, Cliente.class);
	}
	
	public static ClienteResponseDto toDto(Cliente cliente) {
		return new ModelMapper().map(cliente, ClienteResponseDto.class);
	}

	private ClienteMapperDto() {
	}
	
}
