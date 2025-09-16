package spring_park_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring_park_api.entity.ClienteVaga;
import spring_park_api.exception.EntityNotFoundException;
import spring_park_api.repository.ClienteVagaRepository;

@Service
public class ClienteVagaService {

	@Autowired
	private ClienteVagaRepository repository;
	
	@Transactional
	public ClienteVaga criar(ClienteVaga clienteVaga) {
		return repository.save(clienteVaga);
	}

	@Transactional(readOnly = true)
	public ClienteVaga buscarPorRecibo(String recibo) {
		return repository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(
				() -> new EntityNotFoundException(String.format("Recibo '%s' não encontrado no sistema ou checkout ja realizado", recibo)));
	}

	@Transactional(readOnly = true)
	public long getTotalDeVezesEstacionamentoCompleto(String cpf) {
		return repository.countByClienteCpfAndDataDeSaidaIsNotNull(cpf);
	}
}
