package med.clift.api.domain.medico;

import med.clift.api.domain.endereco.Endereco;

public record MedicoAtualDTO(Long id, String nome, String email, String crm, String telefone, Especialidade especialidade, Endereco endereco) {

    public MedicoAtualDTO(Medico medico) {
        this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getTelefone(), medico.getEspecialidade(), medico.getEndereco());
    }
}
