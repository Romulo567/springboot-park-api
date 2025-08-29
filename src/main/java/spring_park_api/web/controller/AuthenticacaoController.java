package spring_park_api.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import spring_park_api.jwt.JwtToken;
import spring_park_api.jwt.JwtUserDetailsService;
import spring_park_api.web.dto.UsuarioLoginDto;
import spring_park_api.web.exception.ErrorMessage;

@RestController
@RequestMapping("/api/v1")
public class AuthenticacaoController {

	@Autowired
	private JwtUserDetailsService detailsService;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private static final Logger log = LoggerFactory.getLogger(AuthenticacaoController.class);
	
	@PostMapping("/auth")
	public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDto dto, HttpServletRequest request){
		log.info("Processo de authenticação pelo login {}", dto.getUsername());
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
			authenticationManager.authenticate(authenticationToken);
			JwtToken token = detailsService.getTokenAuthenticated(dto.getUsername());
			return ResponseEntity.ok(token);
		} catch(AuthenticationException e) {
			log.warn("Bad Credentials from username '{}'", dto.getUsername());
		}
		return ResponseEntity.badRequest().body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais invalidas"));
		
	}
}
