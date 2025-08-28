package spring_park_api.jwt;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import spring_park_api.entity.Usuario;

public class JwtUserDetails extends User{

	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	
	public JwtUserDetails(Usuario usuario) {
		super(usuario.getUsername(), usuario.getPassword(), AuthorityUtils.createAuthorityList(usuario.getRole().name()));
		this.usuario = usuario;
	}
	
	public Long getId() {
		return this.usuario.getId();
	}
	
	public String getRole() {
		return this.usuario.getRole().name();
	}

}
