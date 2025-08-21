package spring_park_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import spring_park_api.entity.Usuario;
import spring_park_api.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Transactional
	public Usuario create(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
}
