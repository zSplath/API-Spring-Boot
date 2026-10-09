package med.clift.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.clift.api.domain.consulta.AgendaDeConsultas;
import med.clift.api.domain.consulta.ConsultaAtualDTO;
import med.clift.api.domain.consulta.ConsultaAgendarDTO;
import med.clift.api.domain.consulta.ConsultaCancelarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {

    @Autowired
    private AgendaDeConsultas agenda;

    @PostMapping
    @Transactional
    public ResponseEntity agendar(@RequestBody @Valid ConsultaAgendarDTO dados) {
        var dto = agenda.agendar(dados);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity cancelar(@RequestBody @Valid ConsultaCancelarDTO dados) {
        agenda.cancelar(dados);
        return ResponseEntity.noContent().build();
    }

}
