package med.clift.api.domain.consulta;

import med.clift.api.domain.consulta.validacoes.ValidadorAgendamentoConsultas;
import med.clift.api.domain.medico.Medico;
import med.clift.api.domain.medico.MedicoRepository;
import med.clift.api.domain.paciente.PacienteRepository;
import med.clift.api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private List<ValidadorAgendamentoConsultas> validadores;

    public ConsultaAtualDTO agendar(ConsultaAgendarDTO dados) {
        if (!pacienteRepository.existsById(dados.idPaciente())) {
            throw new ValidacaoException("Id do paciente não existe");
        }
        if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
            throw new ValidacaoException("Id do medico não existe");
        }

        validadores.forEach(v-> v.validar(dados));

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        var medico = escolherMedico(dados);
        if (medico == null) {
            throw new ValidacaoException("Nenhum medico selecionado");
        }
        var consulta = new Consulta(null, medico, paciente, dados.data(), null);
        consultaRepository.save(consulta);

        return new ConsultaAtualDTO(consulta);
    }

    public void cancelar(ConsultaCancelarDTO dados) {
        if (!consultaRepository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
    }

    private Medico escolherMedico(ConsultaAgendarDTO dados) {
        if (dados.idMedico() != null) {
            return medicoRepository.getReferenceById(dados.idMedico());
        }
        if (dados.especialidade() == null) {
            throw new ValidacaoException("Especialidade não informada para escolha de médico");
        }

        return medicoRepository.escolherMedicoAleatorioParaDataConsulta(dados.especialidade(), dados.data());
    }
}
