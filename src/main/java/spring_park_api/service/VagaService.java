package spring_park_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring_park_api.entity.Vaga;
import spring_park_api.exception.CodigoUniqueViolationException;
import spring_park_api.exception.EntityNotFoundException;
import spring_park_api.repository.VagaRepository;

@Service
public class VagaService {

	@Autowired
	private VagaRepository vagaRepository;
	
	@Transactional
	public Vaga criar(Vaga vaga) {
		try {
			return vagaRepository.save(vaga);
		} catch (DataIntegrityViolationException e) {
			throw new CodigoUniqueViolationException(
					String.format("Vaga com codigo '%s' ja cadastrada", vaga.getCodigo()));
		}
	}
	
	@Transactional(readOnly = true)
	public Vaga buscarPorCodigo(String codigo) {
		return vagaRepository.findByCodigo(codigo).orElseThrow(
				() -> new EntityNotFoundException(String.format("Vaga com codigo '%s' não foi encontrada", codigo)));
	}

	@Transactional(readOnly = true)
	public Vaga buscarPorVagaLivre() {
		return vagaRepository.findFirstByStatus(Vaga.StatusVaga.LIVRE).orElseThrow(
				() -> new EntityNotFoundException("Nenhuma vaga livre foi encontrada"));
	}
	
}
