package med.clift.api.domain.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import med.clift.api.domain.endereco.EnderecoDTO;

public record PacienteDTO(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String telefone,
        @NotBlank @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}") String cpf,
        @NotNull @Valid EnderecoDTO endereco) {
}
