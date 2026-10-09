package med.clift.api.domain.consulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import med.clift.api.domain.medico.Especialidade;

import java.time.LocalDateTime;

public record ConsultaAgendarDTO(
        Long idMedico,

        @NotNull
        Long idPaciente,

        @NotNull
        @Future
        LocalDateTime data,

        Especialidade especialidade) {
}