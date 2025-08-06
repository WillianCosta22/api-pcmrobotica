package comApi.controllers;

import comApi.models.Ativo;
import comApi.services.AtivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ativos")
@CrossOrigin(origins = "*")
public class AtivoController {

    @Autowired
    private AtivoService ativoService;

    @GetMapping
    public ResponseEntity<List<Ativo>> listarTodos(
            @RequestParam(required = false) Ativo.Status status,
            @RequestParam(required = false) Ativo.TipoManutencao tipoManutencao,
            @RequestParam(required = false) Ativo.Alocacao alocacao) {

        List<Ativo> ativos;
        if (status != null || tipoManutencao != null || alocacao != null) {
            ativos = ativoService.filtrar(status, tipoManutencao, alocacao);
        } else {
            ativos = ativoService.listarTodos();
        }

        return ResponseEntity.ok(ativos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ativo> buscarPorId(@PathVariable Long id) {
        Optional<Ativo> ativo = ativoService.buscarPorId(id);
        return ativo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Ativo> buscarPorCodigo(@PathVariable String codigo) {
        Optional<Ativo> ativo = ativoService.buscarPorCodigo(codigo);
        return ativo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ativo> criar(@Valid @RequestBody Ativo ativo) {
        try {
            Ativo novoAtivo = ativoService.salvar(ativo);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAtivo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ativo> atualizar(@PathVariable Long id, @Valid @RequestBody Ativo ativo) {
        Optional<Ativo> ativoExistente = ativoService.buscarPorId(id);
        if (ativoExistente.isPresent()) {
            ativo.setId(id);
            Ativo ativoAtualizado = ativoService.salvar(ativo);
            return ResponseEntity.ok(ativoAtualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Optional<Ativo> ativo = ativoService.buscarPorId(id);
        if (ativo.isPresent()) {
            ativoService.excluir(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/manutencao-vencida")
    public ResponseEntity<List<Ativo>> buscarManutencaoVencida() {
        List<Ativo> ativos = ativoService.buscarManutencaoVencida();
        return ResponseEntity.ok(ativos);
    }

    @GetMapping("/manutencao-proxima")
    public ResponseEntity<List<Ativo>> buscarManutencaoProxima(
            @RequestParam(defaultValue = "7") int dias) {
        List<Ativo> ativos = ativoService.buscarManutencaoProxima(dias);
        return ResponseEntity.ok(ativos);
    }

    @PutMapping("/{id}/finalizar-manutencao")
    public ResponseEntity<Ativo> finalizarManutencao(@PathVariable Long id,
                                                     @RequestBody FinalizarManutencaoRequest request) {
        Ativo ativo = ativoService.finalizarManutencao(id, request.getStatus());
        if (ativo != null) {
            return ResponseEntity.ok(ativo);
        }
        return ResponseEntity.notFound().build();
    }

    public static class FinalizarManutencaoRequest {
        private Ativo.Status status;

        public Ativo.Status getStatus() { return status; }
        public void setStatus(Ativo.Status status) { this.status = status; }
    }
}