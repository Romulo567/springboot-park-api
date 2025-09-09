package spring_park_api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring_park_api.entity.Cliente;
import spring_park_api.exception.CpfUniqueViolationException;
import spring_park_api.exception.EntityNotFoundException;
import spring_park_api.repository.ClienteRepository;
import spring_park_api.repository.projection.ClienteProjection;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Transactional
	public Cliente  criar(Cliente cliente) {
		try {
			return clienteRepository.save(cliente);
		}catch(DataIntegrityViolationException e) {
			throw new CpfUniqueViolationException(String.format("CPF '%s' não pode ser cadastrado, ja existe no sistema" , cliente.getCpf()));
		}
	}
	
	@Transactional(readOnly = true)
	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id).orElseThrow(
				()  -> new EntityNotFoundException(String.format("Cliente id=%s não encontrado no sistema", id))
		);
	}

	@Transactional(readOnly = true)
	public Page<ClienteProjection> buscarTodos(Pageable pageable) {
		return clienteRepository.findAllPageable(pageable);
	}

	@Transactional(readOnly = true)
	public Cliente buscarPorUsuarioId(Long id) {
		return clienteRepository.findByUsuarioId(id);
	}
}
