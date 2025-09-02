package spring_park_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import spring_park_api.jwt.JwtAuthenticationEntryPoint;
import spring_park_api.jwt.JwtAuthorizationFilter;
import spring_park_api.web.exception.ErrorMessage;

@EnableMethodSecurity
@EnableWebMvc
@Configuration
public class SpringSecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthorizationFilter jwtAuthorizationFilter) throws Exception {
	    return http
	        .csrf(csrf -> csrf.disable())
	        .formLogin(form -> form.disable())
	        .httpBasic(basic -> basic.disable())
	        .authorizeHttpRequests(auth -> {
	            auth.requestMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll();
	            auth.requestMatchers(HttpMethod.POST, "/api/v1/auth").permitAll();
	            auth.anyRequest().authenticated();
	        })
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .securityContext(context -> context.securityContextRepository(new RequestAttributeSecurityContextRepository()))
	        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
	        .exceptionHandling(ex -> ex
	            .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
	            .accessDeniedHandler((request, response, accessDeniedException) -> {
	                
	                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

	                ErrorMessage errorBody = new ErrorMessage(request, HttpStatus.FORBIDDEN, "Acesso Negado.");

	                response.getWriter().write(new ObjectMapper().writeValueAsString(errorBody));
	            })
	            )
	        .build();
	}
	
	@Bean
	public JwtAuthorizationFilter jwtAuthorizationFilter() {
		return new JwtAuthorizationFilter();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean 
	AuthenticationManager authenticationMenager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
