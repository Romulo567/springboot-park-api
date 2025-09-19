package spring_park_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring_park_api.entity.Vaga;
import spring_park_api.exception.CodigoUniqueViolationException;
import spring_park_api.exception.EntityNotFoundException;
import spring_park_api.exception.VagaDisponivelException;
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
			throw new CodigoUniqueViolationException("vaga", vaga.getCodigo());
		}
	}
	
	@Transactional(readOnly = true)
	public Vaga buscarPorCodigo(String codigo) {
		return vagaRepository.findByCodigo(codigo).orElseThrow(
				() -> new EntityNotFoundException("Vaga", codigo));
	}

	@Transactional(readOnly = true)
	public Vaga buscarPorVagaLivre() {
		return vagaRepository.findFirstByStatus(Vaga.StatusVaga.LIVRE).orElseThrow(
				() -> new VagaDisponivelException("Nenhuma vaga livre foi encontrada"));
	}
	
}
