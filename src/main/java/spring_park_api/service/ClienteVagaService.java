package spring_park_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring_park_api.entity.ClienteVaga;
import spring_park_api.exception.EntityNotFoundException;
import spring_park_api.repository.ClienteVagaRepository;
import spring_park_api.repository.projection.ClienteVagaProjection;

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
		return repository.countByClienteCpfAndDataSaidaIsNotNull(cpf);
	}

	@Transactional(readOnly = true)
	public Page<ClienteVagaProjection> buscarTodosPorClienteCpf(String cpf, Pageable pageable) {
		return repository.findAllByClienteCpf(cpf, pageable);
	}
	
	@Transactional(readOnly = true)
	public Page<ClienteVagaProjection> buscarTodosPorUsuarioId(Long id, Pageable pageable) {
		return repository.findAllByClienteUsuarioId(id, pageable);
	}
}
