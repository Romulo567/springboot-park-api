package spring_park_api.web.dto.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import spring_park_api.entity.Usuario;
import spring_park_api.web.dto.UsuarioCreateDTO;
import spring_park_api.web.dto.UsuarioResponseDTO;

public class UsuarioMapper {

	public static Usuario toUsuario(UsuarioCreateDTO creatDto) {
		return new ModelMapper().map(creatDto, Usuario.class);
	}
	
	public static UsuarioResponseDTO toDto(Usuario usuario) {
		String role = usuario.getRole().name().substring("ROLE_".length());
		PropertyMap<Usuario, UsuarioResponseDTO> props = new PropertyMap<Usuario, UsuarioResponseDTO>() {
			
			@Override
			protected void configure() {
				map().setRole(role);
			}
		};
		ModelMapper mapper = new ModelMapper();
		mapper.addMappings(props);
		return mapper.map(usuario, UsuarioResponseDTO.class);
	}
}
