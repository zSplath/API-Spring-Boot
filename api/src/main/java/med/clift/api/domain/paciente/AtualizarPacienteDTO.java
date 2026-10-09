package med.clift.api.domain.paciente;

import jakarta.validation.Valid;
import med.clift.api.domain.endereco.EnderecoDTO;

public record AtualizarPacienteDTO(
    Long id,
    String nome,
    String telefone,
    @Valid EnderecoDTO endereco
) {
}
