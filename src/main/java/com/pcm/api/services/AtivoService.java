package com.pcm.api.services;

import com.pcm.api.models.Ativo;
import com.pcm.api.repositories.AtivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AtivoService {

    @Autowired
    private AtivoRepository ativoRepository;

    public List<Ativo> listarTodos() {
        return ativoRepository.findAll();
    }

    public Optional<Ativo> buscarPorId(Long id) {
        return ativoRepository.findById(id);
    }

    public Optional<Ativo> buscarPorCodigo(String codigo) {
        return ativoRepository.findByCodigoAtivo(codigo);
    }

    public Ativo salvar(Ativo ativo) {
        return ativoRepository.save(ativo);
    }

    public void excluir(Long id) {
        ativoRepository.deleteById(id);
    }

    public List<Ativo> filtrar(Ativo.Status status, Ativo.TipoManutencao tipoManutencao, Ativo.Alocacao alocacao) {
        return ativoRepository.findWithFilters(status, tipoManutencao, alocacao);
    }

    public List<Ativo> buscarManutencaoVencida() {
        return ativoRepository.findAtivosComManutencaoVencida(LocalDate.now());
    }

    public List<Ativo> buscarManutencaoProxima(int dias) {
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.plusDays(dias);
        return ativoRepository.findAtivosComManutencaoProxima(hoje, dataLimite);
    }

    public Ativo finalizarManutencao(Long id, Ativo.Status novoStatus) {
        Optional<Ativo> ativoOpt = ativoRepository.findById(id);
        if (ativoOpt.isPresent()) {
            Ativo ativo = ativoOpt.get();
            ativo.setStatus(novoStatus);
            ativo.setUltimaManutencao(LocalDate.now());
            return ativoRepository.save(ativo);
        }
        return null;
    }
}