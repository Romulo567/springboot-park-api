package spring_park_api.web.dto.mapper;

import org.modelmapper.ModelMapper;

import spring_park_api.entity.Vaga;
import spring_park_api.web.dto.VagaCreateDto;
import spring_park_api.web.dto.VagaResponseDto;

public class VagaMapper {

	public static Vaga toVaga(VagaCreateDto dto) {
		return new ModelMapper().map(dto, Vaga.class);
	}
	
	public static VagaResponseDto toDto(Vaga vaga) {
		return new ModelMapper().map(vaga, VagaResponseDto.class);
	}

	private VagaMapper() {
	}
}
