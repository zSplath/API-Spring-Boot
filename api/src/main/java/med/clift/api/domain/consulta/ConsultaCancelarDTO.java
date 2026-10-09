package med.clift.api.domain.consulta;

import jakarta.validation.constraints.NotNull;

public record ConsultaCancelarDTO(
        @NotNull
        Long idConsulta,

        @NotNull
        MotivoCancelamento motivo) {
}
