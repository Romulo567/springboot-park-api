package spring_park_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstacionamentoService {

	@Autowired
	private ClienteVagaService clienteVagaService;
	
	@Autowired
	private ClienteService clienteSservice;
	
	@Autowired
	private VagaService vagaService;
	
	
}
