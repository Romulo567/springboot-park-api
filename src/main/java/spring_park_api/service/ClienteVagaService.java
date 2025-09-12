package spring_park_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring_park_api.entity.ClienteVaga;
import spring_park_api.repository.ClienteVagaRepository;

@Service
public class ClienteVagaService {

	@Autowired
	private ClienteVagaRepository repository;
	
	@Transactional
	public ClienteVaga criar(ClienteVaga clienteVaga) {
		return repository.save(clienteVaga);
	}
}
