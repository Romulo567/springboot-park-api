package spring_park_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring_park_api.entity.Usuario;
import spring_park_api.exception.EntityNotFoundException;
import spring_park_api.exception.PasswordInvalidException;
import spring_park_api.exception.UserNameUniqueViolationException;
import spring_park_api.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Transactional
	public Usuario criar(Usuario usuario) {
		try {
		return usuarioRepository.save(usuario);
		} catch(DataIntegrityViolationException e) {
			throw new UserNameUniqueViolationException(String.format("Username {%s} ja cadastrado", usuario.getUsername()));
		}
	}
	
	@Transactional(readOnly = true)
	public Usuario buscarPorId(Long id) {
		return usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Usuario id = {%s} não encontrado", id)));
	}
	
	@Transactional
	public Usuario atualizarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {
		if(!novaSenha.equals(confirmaSenha)) {
			throw new PasswordInvalidException("Nova senha não confere com a confirmação de senha.");
		}
		
		Usuario user = buscarPorId(id);
		if(!user.getPassword().equals(senhaAtual)) {
			throw new PasswordInvalidException("Sua senha não confere");
		}
		user.setPassword(novaSenha);
		return user;
	}

	@Transactional(readOnly = true)
	public List<Usuario> buscarTodos() {
		return usuarioRepository.findAll();
	}
}
