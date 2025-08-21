package spring_park_api.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring_park_api.service.UsuarioService;

@RestController
@RequestMapping(value = "api/v1/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
}
