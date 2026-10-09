package med.clift.api.domain.consulta.validacoes;

import med.clift.api.domain.consulta.ConsultaAgendarDTO;
import med.clift.api.infra.exception.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorHorarioFuncionamentoClinica implements ValidadorAgendamentoConsultas{

    public void validar(ConsultaAgendarDTO dados) {
        var dataConsulta = dados.data();

        var isDomingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var isAntesAbertura = dataConsulta.getHour() < 7;
        var isUltimoHorario = dataConsulta.getHour() > 18;

        if (isDomingo || isAntesAbertura || isUltimoHorario) {
            throw new ValidacaoException("Fora do Horário permitido");
        }
    }
}
