package comApi.services;

import comApi.models.OrdemServico;
import comApi.repositories.OrdemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrdemServicoService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    public List<OrdemServico> listarTodas() {
        return ordemServicoRepository.findAll();
    }

    public Optional<OrdemServico> buscarPorId(Long id) {
        return ordemServicoRepository.findById(id);
    }

    public List<OrdemServico> buscarPorAtivo(Long ativoId) {
        return ordemServicoRepository.findByAtivoId(ativoId);
    }

    public List<OrdemServico> buscarPorTecnico(String tecnico) {
        return ordemServicoRepository.findByTecnicoResponsavel(tecnico);
    }

    public List<OrdemServico> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return ordemServicoRepository.findByPeriodo(inicio, fim);
    }

    public OrdemServico salvar(OrdemServico ordemServico) {
        return ordemServicoRepository.save(ordemServico);
    }

    public void excluir(Long id) {
        ordemServicoRepository.deleteById(id);
    }
}