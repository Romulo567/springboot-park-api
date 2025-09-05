package spring_park_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import spring_park_api.entity.Cliente;
import spring_park_api.exception.CpfUniqueViolationException;
import spring_park_api.repository.ClienteRepository;

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
}
