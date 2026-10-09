package med.clift.api.domain.medico;

import jakarta.validation.constraints.NotNull;
import med.clift.api.domain.endereco.EnderecoDTO;

public record AtualizarMedicolDTO(
        @NotNull Long id,
        String nome,
        String telefone,
        EnderecoDTO endereco) {
}
