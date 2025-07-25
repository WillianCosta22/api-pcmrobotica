package com.pcm.api.repositories;

import com.pcm.api.models.Ativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AtivoRepository extends JpaRepository<Ativo, Long> {

    Optional<Ativo> findByCodigoAtivo(String codigoAtivo);

    List<Ativo> findByStatus(Ativo.Status status);

    List<Ativo> findByTipoManutencao(Ativo.TipoManutencao tipoManutencao);

    List<Ativo> findByAlocacao(Ativo.Alocacao alocacao);

    @Query("SELECT a FROM Ativo a WHERE a.proximaManutencao <= :data")
    List<Ativo> findAtivosComManutencaoVencida(@Param("data") LocalDate data);

    @Query("SELECT a FROM Ativo a WHERE a.proximaManutencao BETWEEN :dataInicio AND :dataFim")
    List<Ativo> findAtivosComManutencaoProxima(@Param("dataInicio") LocalDate dataInicio,
                                               @Param("dataFim") LocalDate dataFim);

    @Query("SELECT a FROM Ativo a WHERE " +
            "(:status IS NULL OR a.status = :status) AND " +
            "(:tipoManutencao IS NULL OR a.tipoManutencao = :tipoManutencao) AND " +
            "(:alocacao IS NULL OR a.alocacao = :alocacao)")
    List<Ativo> findWithFilters(@Param("status") Ativo.Status status,
                                @Param("tipoManutencao") Ativo.TipoManutencao tipoManutencao,
                                @Param("alocacao") Ativo.Alocacao alocacao);
}