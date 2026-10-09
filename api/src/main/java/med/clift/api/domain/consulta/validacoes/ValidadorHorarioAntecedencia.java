package med.clift.api.domain.consulta.validacoes;

import med.clift.api.domain.consulta.ConsultaAgendarDTO;
import med.clift.api.infra.exception.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoConsultas{

    public void validar(ConsultaAgendarDTO dados) {
            var diferencaEmMinutos = Duration.between(LocalDateTime.now(), dados.data()).toMinutes();

            if (diferencaEmMinutos < 30) {
                throw new ValidacaoException("Necessário agendar com 30 minutos de Antecedência");
            }
    }
}
