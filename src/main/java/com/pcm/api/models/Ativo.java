package com.pcm.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ativos")
public class Ativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_ativo", unique = true, nullable = false)
    @JsonProperty("codigo_ativo")
    @NotBlank(message = "Código do ativo é obrigatório")
    @Size(max = 20, message = "Código deve ter no máximo 20 caracteres")
    private String codigoAtivo;

    @Column(nullable = false)
    @NotBlank(message = "Denominação é obrigatória")
    @Size(max = 255, message = "Denominação deve ter no máximo 255 caracteres")
    private String denominacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_manutencao", nullable = false)
    @JsonProperty("tipo_manutencao")
    private TipoManutencao tipoManutencao = TipoManutencao.mensal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Alocacao alocacao = Alocacao.Cetrel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.operacional;

    @Column(name = "ultima_manutencao")
    @JsonProperty("ultima_manutencao")
    private LocalDate ultimaManutencao;

    @Column(name = "proxima_manutencao")
    @JsonProperty("proxima_manutencao")
    private LocalDate proximaManutencao;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "data_criacao")
    @JsonProperty("data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    @JsonProperty("data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Enums
    public enum TipoManutencao {
        mensal, trimestral
    }

    public enum Alocacao {
        Cetrel, Braskem, Camaçari,
        @JsonProperty("Polo Petroquímico")
        Polo_Petroquímico, Outro
    }

    public enum Status {
        operacional, inoperante,
        @JsonProperty("fora de serviço")
        fora_de_servico
    }

    // Construtores
    public Ativo() {}

    public Ativo(String codigoAtivo, String denominacao, TipoManutencao tipoManutencao,
                 Alocacao alocacao, Status status, LocalDate ultimaManutencao, String observacoes) {
        this.codigoAtivo = codigoAtivo;
        this.denominacao = denominacao;
        this.tipoManutencao = tipoManutencao;
        this.alocacao = alocacao;
        this.status = status;
        this.ultimaManutencao = ultimaManutencao;
        this.observacoes = observacoes;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        calcularProximaManutencao();
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
        calcularProximaManutencao();
    }

    private void calcularProximaManutencao() {
        if (ultimaManutencao != null && tipoManutencao != null) {
            switch (tipoManutencao) {
                case mensal:
                    proximaManutencao = ultimaManutencao.plusDays(30);
                    break;
                case trimestral:
                    proximaManutencao = ultimaManutencao.plusDays(90);
                    break;
            }
        }
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoAtivo() { return codigoAtivo; }
    public void setCodigoAtivo(String codigoAtivo) { this.codigoAtivo = codigoAtivo; }

    public String getDenominacao() { return denominacao; }
    public void setDenominacao(String denominacao) { this.denominacao = denominacao; }

    public TipoManutencao getTipoManutencao() { return tipoManutencao; }
    public void setTipoManutencao(TipoManutencao tipoManutencao) {
        this.tipoManutencao = tipoManutencao;
        calcularProximaManutencao();
    }

    public Alocacao getAlocacao() { return alocacao; }
    public void setAlocacao(Alocacao alocacao) { this.alocacao = alocacao; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDate getUltimaManutencao() { return ultimaManutencao; }
    public void setUltimaManutencao(LocalDate ultimaManutencao) {
        this.ultimaManutencao = ultimaManutencao;
        calcularProximaManutencao();
    }

    public LocalDate getProximaManutencao() { return proximaManutencao; }
    public void setProximaManutencao(LocalDate proximaManutencao) { this.proximaManutencao = proximaManutencao; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}