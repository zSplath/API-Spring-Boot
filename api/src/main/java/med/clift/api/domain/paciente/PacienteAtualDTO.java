package med.clift.api.domain.paciente;

import med.clift.api.domain.endereco.Endereco;

public record PacienteAtualDTO(String nome, String email, String telefone, String cpf, Endereco endereco) {
    public PacienteAtualDTO(Paciente paciente) {
        this(paciente.getNome(), paciente.getEmail(), paciente.getTelefone(), paciente.getCpf(), paciente.getEndereco());
    }
}