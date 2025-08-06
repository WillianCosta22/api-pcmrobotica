package comApi.repositories;

import comApi.models.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    List<OrdemServico> findByAtivoId(Long ativoId);

    List<OrdemServico> findByTecnicoResponsavel(String tecnicoResponsavel);

    @Query("SELECT os FROM OrdemServico os WHERE os.dataExecucao BETWEEN :dataInicio AND :dataFim")
    List<OrdemServico> findByPeriodo(@Param("dataInicio") LocalDateTime dataInicio,
                                     @Param("dataFim") LocalDateTime dataFim);
}