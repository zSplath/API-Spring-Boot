package med.clift.api.domain.consulta.validacoes;

import med.clift.api.domain.consulta.ConsultaAgendarDTO;

public interface ValidadorAgendamentoConsultas {

    void validar (ConsultaAgendarDTO dados);
}
