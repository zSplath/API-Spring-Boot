package med.clift.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import med.clift.api.domain.endereco.EnderecoDTO;

public record MedicoDTO(
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email,

        @NotBlank
        String telefone,

        @NotBlank
        @Pattern(regexp = "\\d{4,6}")
        String crm,
        @NotNull
        Especialidade especialidade,

        @NotNull @Valid EnderecoDTO endereco) {
}
