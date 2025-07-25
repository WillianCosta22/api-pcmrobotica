package com.pcm.api.controllers;

import com.pcm.api.models.Ativo;
import com.pcm.api.models.OrdemServico;
import com.pcm.api.services.AtivoService;
import com.pcm.api.services.OrdemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordens-servico")
@CrossOrigin(origins = "*")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService ordemServicoService;

    @Autowired
    private AtivoService ativoService;

    @GetMapping
    public ResponseEntity<List<OrdemServico>> listarTodas() {
        List<OrdemServico> ordensServico = ordemServicoService.listarTodas();
        return ResponseEntity.ok(ordensServico);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServico> buscarPorId(@PathVariable Long id) {
        Optional<OrdemServico> ordemServico = ordemServicoService.buscarPorId(id);
        return ordemServico.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ativo/{ativoId}")
    public ResponseEntity<List<OrdemServico>> buscarPorAtivo(@PathVariable Long ativoId) {
        List<OrdemServico> ordensServico = ordemServicoService.buscarPorAtivo(ativoId);
        return ResponseEntity.ok(ordensServico);
    }

    @GetMapping("/tecnico/{tecnico}")
    public ResponseEntity<List<OrdemServico>> buscarPorTecnico(@PathVariable String tecnico) {
        List<OrdemServico> ordensServico = ordemServicoService.buscarPorTecnico(tecnico);
        return ResponseEntity.ok(ordensServico);
    }

    @PostMapping
    public ResponseEntity<OrdemServico> criar(@Valid @RequestBody CriarOrdemServicoRequest request) {
        try {
            Optional<Ativo> ativoOpt = ativoService.buscarPorId(request.getAtivoId());
            if (!ativoOpt.isPresent()) {
                return ResponseEntity.badRequest().build();
            }

            Ativo ativo = ativoOpt.get();
            OrdemServico ordemServico = new OrdemServico();
            ordemServico.setAtivo(ativo);
            ordemServico.setResumoServico(request.getResumoServico());
            ordemServico.setStatusAnterior(request.getStatusAnterior());
            ordemServico.setStatusNovo(request.getStatusNovo());
            ordemServico.setTecnicoResponsavel(request.getTecnicoResponsavel());

            OrdemServico novaOrdem = ordemServicoService.salvar(ordemServico);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaOrdem);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Optional<OrdemServico> ordemServico = ordemServicoService.buscarPorId(id);
        if (ordemServico.isPresent()) {
            ordemServicoService.excluir(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public static class CriarOrdemServicoRequest {
        private Long ativoId;
        private String resumoServico;
        private Ativo.Status statusAnterior;
        private Ativo.Status statusNovo;
        private String tecnicoResponsavel;

        // Getters e Setters
        public Long getAtivoId() { return ativoId; }
        public void setAtivoId(Long ativoId) { this.ativoId = ativoId; }

        public String getResumoServico() { return resumoServico; }
        public void setResumoServico(String resumoServico) { this.resumoServico = resumoServico; }

        public Ativo.Status getStatusAnterior() { return statusAnterior; }
        public void setStatusAnterior(Ativo.Status statusAnterior) { this.statusAnterior = statusAnterior; }

        public Ativo.Status getStatusNovo() { return statusNovo; }
        public void setStatusNovo(Ativo.Status statusNovo) { this.statusNovo = statusNovo; }

        public String getTecnicoResponsavel() { return tecnicoResponsavel; }
        public void setTecnicoResponsavel(String tecnicoResponsavel) { this.tecnicoResponsavel = tecnicoResponsavel; }
    }
}