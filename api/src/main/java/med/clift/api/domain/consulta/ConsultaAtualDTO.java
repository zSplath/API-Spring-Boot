package med.clift.api.domain.consulta;

import java.time.LocalDateTime;

public record ConsultaAtualDTO(Long id, Long idMedico, Long idPaciente, LocalDateTime data) {
    public ConsultaAtualDTO(Consulta consulta) {
        this(consulta.getId(), consulta.getMedico().getId(), consulta.getPaciente().getId(), consulta.getData());
    }
}
