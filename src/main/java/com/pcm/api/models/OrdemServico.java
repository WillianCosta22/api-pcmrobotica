package com.pcm.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordens_servico")
public class OrdemServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id", nullable = false)
    @JsonProperty("ativo_id")
    @NotNull(message = "Ativo é obrigatório")
    private Ativo ativo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_ordem")
    @JsonProperty("tipo_ordem")
    private TipoOrdem tipoOrdem = TipoOrdem.preventiva;

    @Column(name = "resumo_servico", columnDefinition = "TEXT")
    @JsonProperty("resumo_servico")
    @NotBlank(message = "Resumo do serviço é obrigatório")
    private String resumoServico;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior")
    @JsonProperty("status_anterior")
    private Ativo.Status statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo")
    @JsonProperty("status_novo")
    private Ativo.Status statusNovo;

    @Column(name = "data_execucao")
    @JsonProperty("data_execucao")
    private LocalDateTime dataExecucao;

    @Column(name = "tecnico_responsavel")
    @JsonProperty("tecnico_responsavel")
    @NotBlank(message = "Técnico responsável é obrigatório")
    private String tecnicoResponsavel;

    public enum TipoOrdem {
        preventiva, corretiva
    }

    // Construtores
    public OrdemServico() {}

    public OrdemServico(Ativo ativo, String resumoServico, Ativo.Status statusAnterior,
                        Ativo.Status statusNovo, String tecnicoResponsavel) {
        this.ativo = ativo;
        this.resumoServico = resumoServico;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.tecnicoResponsavel = tecnicoResponsavel;
    }

    @PrePersist
    protected void onCreate() {
        dataExecucao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Ativo getAtivo() { return ativo; }
    public void setAtivo(Ativo ativo) { this.ativo = ativo; }

    public TipoOrdem getTipoOrdem() { return tipoOrdem; }
    public void setTipoOrdem(TipoOrdem tipoOrdem) { this.tipoOrdem = tipoOrdem; }

    public String getResumoServico() { return resumoServico; }
    public void setResumoServico(String resumoServico) { this.resumoServico = resumoServico; }

    public Ativo.Status getStatusAnterior() { return statusAnterior; }
    public void setStatusAnterior(Ativo.Status statusAnterior) { this.statusAnterior = statusAnterior; }

    public Ativo.Status getStatusNovo() { return statusNovo; }
    public void setStatusNovo(Ativo.Status statusNovo) { this.statusNovo = statusNovo; }

    public LocalDateTime getDataExecucao() { return dataExecucao; }
    public void setDataExecucao(LocalDateTime dataExecucao) { this.dataExecucao = dataExecucao; }

    public String getTecnicoResponsavel() { return tecnicoResponsavel; }
    public void setTecnicoResponsavel(String tecnicoResponsavel) { this.tecnicoResponsavel = tecnicoResponsavel; }
}