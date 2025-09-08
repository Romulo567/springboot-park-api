package spring_park_api.web.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import spring_park_api.web.dto.PageableDto;

public class PageableMapper {

	public static PageableDto toDto(Page<?> page) {
		return new ModelMapper().map(page, PageableDto.class);
	}

	private PageableMapper() {
	}
	
	
}
